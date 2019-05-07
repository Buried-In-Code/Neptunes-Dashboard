package macro.dashboard.neptunes.team

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.*
import macro.dashboard.neptunes.BadRequestException
import macro.dashboard.neptunes.ConflictException
import macro.dashboard.neptunes.NotFoundException
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.team.TeamTable.update
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object TeamController {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	fun Route.teamRoutes() {
		route(path = "/{gameID}/teams") {
			get {
				val gameID = call.parameters["gameID"]?.toLongOrNull()
					?: GameTable.selectLatest()?.ID
					?: throw NotFoundException(message = "Game Not Found")
				val teams = TeamTable.searchByGame(gameID = gameID)
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
				val gameID =
					call.parameters["gameID"]?.toLongOrNull()
						?: GameTable.selectLatest()?.ID
						?: throw NotFoundException(message = "Game Not Found")
				val request = call.receiveOrNull<TeamRequest>()
					?: throw BadRequestException(message = "A body is required")
				if (request.name == "")
					throw BadRequestException(message = "name is required")
				var found = TeamTable.select(gameID = gameID, name = request.name)
				if (found != null)
					throw ConflictException(message = "A Team with the given Name: '${request.name}' already exists")
				TeamTable.insert(gameID = gameID, name = request.name)
				found = TeamTable.select(gameID = gameID, name = request.name)
					?: throw NotFoundException(message = "Something has gone Wrong read the logs, call the wizard")
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
				if (request.name == "")
					throw BadRequestException(message = "name is required")
				TeamTable.select(ID = ID)?.update(name = request.name)
					?: throw NotFoundException(message = "No Team was found with the given ID '$ID'")
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

class TeamRequest(val name: String)