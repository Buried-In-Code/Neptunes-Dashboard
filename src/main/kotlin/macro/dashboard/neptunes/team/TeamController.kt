package macro.dashboard.neptunes.team

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.*
import macro.dashboard.neptunes.BadRequestException
import macro.dashboard.neptunes.ConflictException
import macro.dashboard.neptunes.NotFoundException
import macro.dashboard.neptunes.UnknownException
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.player.PlayerTable.update
import macro.dashboard.neptunes.team.TeamTable.update
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object TeamController {
	private val LOGGER: Logger = LogManager.getLogger(TeamController::class.java)

	fun Route.teamRoutes() {
		route(path = "/{gameID}/teams") {
			get {
				val gameID = call.parameters["gameID"]?.toLongOrNull()
				val name = call.request.queryParameters["name"] ?: "%"
				val teams = TeamTable.search(gameID = gameID, name = name)
				call.respond(
					message = teams.filterNot { it.getPlayers().isEmpty() }.map {
						it.toOutput(
							showGame = false,
							showPlayers = true
						)
					},
					status = HttpStatusCode.OK
				)
			}
			post {
				val gameID = call.parameters["gameID"]?.toLongOrNull()
				val request = call.receiveOrNull<TeamRequest>()
					?: throw BadRequestException(message = "A body is required")
				if (request.name == "")
					throw BadRequestException(message = "name is required")
				var found = TeamTable.search(gameID = gameID, name = request.name).firstOrNull()
				if (found != null)
					throw ConflictException(message = "A Team with the given Name: '${request.name}' already exists")
				TeamTable.insert(gameID = gameID, name = request.name)
				found = TeamTable.search(gameID = gameID, name = request.name).firstOrNull()
					?: throw UnknownException(message = "Something has gone Wrong read the logs, call the wizard")
				request.players.forEach { playerID ->
					PlayerTable.select(ID = playerID)?.update(teamID = found.ID)
				}
				call.respond(
					message = found.toOutput(showGame = true, showPlayers = true),
					status = HttpStatusCode.Created
				)
			}
		}
		route(path = "/teams/{ID}") {
			get {
				val ID = call.parameters["ID"]?.toIntOrNull()
					?: throw BadRequestException(message = "Invalid ID")
				val team = TeamTable.select(ID = ID)
					?: throw NotFoundException(message = "No Team was found with the given ID '$ID'")
				call.respond(
					message = team.toOutput(showGame = true, showPlayers = true),
					status = HttpStatusCode.OK
				)
			}
			put {
				val ID = call.parameters["ID"]?.toIntOrNull()
					?: throw BadRequestException(message = "Invalid ID")
				val request = call.receiveOrNull<TeamRequest>()
					?: throw BadRequestException(message = "A body is required")
				TeamTable.select(ID = ID)?.update(name = request.name)
					?: throw NotFoundException(message = "No Team was found with the given ID '$ID'")
				request.players.forEach { playerID ->
					PlayerTable.select(ID = playerID)?.update(teamID = ID)
				}
				val team = TeamTable.select(ID = ID)
					?: throw NotFoundException(message = "No Team was found with the given ID '$ID'")
				call.respond(
					message = team.toOutput(showGame = true, showPlayers = true),
					status = HttpStatusCode.OK
				)
			}
		}
	}
}

class TeamRequest(val name: String, val players: List<Int> = emptyList())