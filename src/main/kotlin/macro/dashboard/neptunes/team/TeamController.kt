package macro.dashboard.neptunes.team

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.*
import macro.dashboard.neptunes.BadRequestException
import macro.dashboard.neptunes.ConflictException
import macro.dashboard.neptunes.NotFoundException
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.team.TeamTable.update
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object TeamController {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	fun Route.teamRoutes() {
		fun ApplicationCall.getGame(): Game {
			val gameID = parameters["game-ID"]?.toLongOrNull()
			return GameTable.select(ID = gameID ?: -1)
				?: throw NotFoundException(message = "No Game was found with the given ID '$gameID'")
		}
		route(path = "/{game-ID}/teams") {
			get {
				val name = call.request.queryParameters["name"] ?: ""
				val teams = TeamTable.searchByGame(gameID = call.getGame().ID).filter {
					LOGGER.debug("${it.name} -> $name")
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
				val game = call.getGame()
				var found = TeamTable.select(gameID = game.ID, name = request.name)
				if (found != null)
					throw ConflictException(message = "A Team with the given Name: '${request.name}' already exists")
				TeamTable.insert(gameID = game.ID, name = request.name)
				found = TeamTable.select(gameID = game.ID, name = request.name)
					?: throw NotFoundException(message = "Something has gone Wrong read the logs, call the wizard")
				call.respond(
					message = found.toOutput(showGame = true, showPlayers = true),
					status = HttpStatusCode.Created
				)
			}
		}
		route(path = "/teams/{team-ID}") {
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
				team.update(name = request.name)
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

class TeamRequest(val name: String)