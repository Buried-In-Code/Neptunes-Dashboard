package macro.dashboard.neptunes.player

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import macro.dashboard.neptunes.BadRequestException
import macro.dashboard.neptunes.NotFoundException
import macro.dashboard.neptunes.player.PlayerTable.update
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object PlayerController {
	private val LOGGER = LogManager.getLogger(PlayerController::class.java)

	fun Route.playerRoutes() {
		route(path = "/{gameID}/players") {
			get {
				val gameID = call.parameters["gameID"]?.toLongOrNull()
				val alias = call.request.queryParameters["alias"] ?: ""
				val players = PlayerTable.search(gameID = gameID, alias = alias)
				call.respond(
					message = players.map { it.toOutput(showGame = false, showTeam = false, showTurns=false) },
					status = HttpStatusCode.OK
				)
			}
		}
		route(path = "/players/{ID}") {
			get {
				val ID = call.parameters["ID"]?.toIntOrNull()
					?: throw BadRequestException(message = "Invalid ID")
				val player = PlayerTable.select(ID = ID)
					?: throw NotFoundException(message = "No Player was found with the given ID '$ID'")
				call.respond(
					message = player.toOutput(showGame = true, showTeam = true),
					status = HttpStatusCode.OK
				)
			}
			put {
				val ID = call.parameters["ID"]?.toIntOrNull()
					?: throw BadRequestException(message = "Invalid ID")
				val request =
					call.receiveOrNull<PlayerRequest>() ?: throw BadRequestException(message = "A body is required")
				if (request.name == "")
					throw BadRequestException(message = "name is required")
				var player = PlayerTable.select(ID = ID)
					?: throw NotFoundException(message = "No Player was found with the given ID '$ID'")
				player.update(name = request.name)
				player = PlayerTable.select(ID = ID)
					?: throw NotFoundException(message = "No Player was found with the given ID '$ID'")
				call.respond(
					message = player.toOutput(showGame = true, showTeam = true),
					status = HttpStatusCode.OK
				)
			}
		}
	}
}

class PlayerRequest(val name: String)