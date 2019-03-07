package macro.dashboard.neptunes.game

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.*
import macro.dashboard.neptunes.BadRequestException
import macro.dashboard.neptunes.ConflictException
import macro.dashboard.neptunes.NotFoundException
import macro.dashboard.neptunes.UnknownException
import macro.dashboard.neptunes.backend.Neptunes
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object GameController {
	private val LOGGER = LogManager.getLogger(GameController::class.java)

	fun Route.gameRoutes() {
		route(path = "/games") {
			get {
				val name = call.request.queryParameters["name"] ?: ""
				val games = GameTable.search(name = name)
				if (games.isEmpty())
					throw NotFoundException(message = "No Games were found with the given name '$name'")
				call.respond(
					message = games.map { it.toOutput() },
					status = HttpStatusCode.OK
				)
			}
			post {
				val request = call.receiveOrNull<GameRequest>() ?: throw BadRequestException(message = "A body is required")
				if(request.ID == 0L)
					throw BadRequestException(message = "ID is required")
				if(request.code == "")
					throw BadRequestException(message = "code is required")
				var found = GameTable.select(ID = request.ID)
				if (found != null)
					throw ConflictException(message = "A Game with the given ID: '${request.ID}' already exists")
				Neptunes.getGame(gameID = request.ID, code = request.code)
				found = GameTable.select(ID = request.ID)
					?: throw UnknownException(message = "Something has gone Wrong read the logs, call the wizard")
				call.respond(
					message = found.toOutput(),
					status = HttpStatusCode.Created
				)
			}
			get(path = "/latest") {
				val game = GameTable.search().firstOrNull()
					?: throw NotFoundException(message = "No Games were found")
				call.respond(
					message = game.toOutput(),
					status = HttpStatusCode.OK
				)
			}
			route(path = "/{ID}") {
				get {
					val param: Long = call.parameters["ID"]?.toLongOrNull()
						?: throw BadRequestException(message = "Invalid ID")
					val game = GameTable.select(ID = param)
						?: throw NotFoundException(message = "No Game was found with the given ID '$param'")
					call.respond(
						message = game.toOutput(),
						status = HttpStatusCode.OK
					)
				}
				put {
					val param: Long = call.parameters["ID"]?.toLongOrNull()
						?: throw BadRequestException(message = "Invalid ID")
					var game = GameTable.select(ID = param)
						?: throw NotFoundException(message = "No Game was found with the given ID '$param'")
					Neptunes.getGame(gameID = game.ID, code = game.code)
					game = GameTable.select(ID = param)
						?: throw NotFoundException(message = "No Game was found with the given ID '$param'")
					call.respond(
						message = game.toOutput(),
						status = HttpStatusCode.OK
					)
				}
			}
		}
	}
}

data class GameRequest(val ID: Long, val code: String)