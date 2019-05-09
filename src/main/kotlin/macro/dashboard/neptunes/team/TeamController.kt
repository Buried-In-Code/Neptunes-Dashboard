package macro.dashboard.neptunes.team

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.*
import macro.dashboard.neptunes.BadRequestException
import macro.dashboard.neptunes.Config.Companion.CONFIG
import macro.dashboard.neptunes.ConflictException
import macro.dashboard.neptunes.NotFoundException
import macro.dashboard.neptunes.team.TeamTable.update
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object TeamController {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	fun Route.teamRoutes() {
		route(path = "/teams") {
			get {
				val name = call.request.queryParameters["name"] ?: ""
				val teams = TeamTable.search().filter {
					it.name.contains(name, ignoreCase = true)
				}
				call.respond(
					message = teams.filterNot { it.players.isEmpty() }.map {
						it.toOutput(
							showGame = false,
							showPlayers = true
						)
					},
					status = HttpStatusCode.OK
				)
			}
			post {
				val request = call.receiveOrNull<TeamRequest>()
					?: throw BadRequestException(message = "A body is required")
				if (request.name == "")
					throw BadRequestException(message = "name is required")
				var found = TeamTable.select(name = request.name)
				if (found != null)
					throw ConflictException(message = "A Team with the given Name: '${request.name}' already exists")
				TeamTable.insert(gameID = CONFIG.gameID, name = request.name)
				found = TeamTable.select(name = request.name)
					?: throw NotFoundException(message = "Something has gone Wrong read the logs, call the wizard")
				call.respond(
					message = found.toOutput(showGame = true, showPlayers = true),
					status = HttpStatusCode.Created
				)
			}
			route(path = "/{team-ID}") {
				fun ApplicationCall.getTeam(): Team {
					val teamID = parameters["team-ID"]?.toIntOrNull()
					return TeamTable.select(ID = teamID ?: -1)
						?: throw NotFoundException(message = "No Team was found with the given ID '$teamID'")
				}
				get {
					call.respond(
						message = call.getTeam().toOutput(showGame = true, showPlayers = true),
						status = HttpStatusCode.OK
					)
				}
				put {
					val request = call.receiveOrNull<TeamRequest>()
						?: throw BadRequestException(message = "A body is required")
					if (request.name == "")
						throw BadRequestException(message = "name is required")
					var team = call.getTeam()
					TeamTable.update(ID = team.ID, name = request.name)
					team = TeamTable.select(ID = team.ID)
						?: throw NotFoundException(message = "No Team was found with the given ID '${team.ID}'")
					call.respond(
						message = team.toOutput(showGame = true, showPlayers = true),
						status = HttpStatusCode.OK
					)
				}
			}
		}
	}
}

class TeamRequest(val name: String)