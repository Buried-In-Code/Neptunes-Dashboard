package macro.dashboard.neptunes.game

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import macro.dashboard.neptunes.BadRequestException
import macro.dashboard.neptunes.Config.Companion.CONFIG
import macro.dashboard.neptunes.ConflictException
import macro.dashboard.neptunes.NotFoundException
import macro.dashboard.neptunes.backend.Proteus
import macro.dashboard.neptunes.backend.Triton
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object GameController {
	private val LOGGER = LogManager.getLogger()

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
					if (Triton.getGame(gameID = game.ID, code = CONFIG.games[param] ?: "") != true) {
						LOGGER.info("Unable to parse trying again as Proteus")
						Proteus.getGame(gameID = game.ID, code = CONFIG.games[param] ?: "")
					}
					game = GameTable.select(ID = param)
						?: throw NotFoundException(message = "No Game was found with the given ID '$param'")
					call.respond(
						message = game.toOutput(),
						status = HttpStatusCode.OK
					)
				}
				post {
					val param: Long = call.parameters["ID"]?.toLongOrNull()
						?: throw BadRequestException(message = "Invalid ID")
					var game = GameTable.select(ID = param)
					if (game != null)
						throw ConflictException(message = "Game with the given ID `$param` already exists")
					if (Triton.getGame(gameID = param, code = CONFIG.games[param] ?: "") != true) {
						LOGGER.info("Unable to parse trying again as Proteus")
						Proteus.getGame(gameID = param, code = CONFIG.games[param] ?: "")
					}
					game = GameTable.select(ID = param)
						?: throw NotFoundException(message = "No Game was found with the given ID '$param'")
					call.respond(
						message = game!!.toOutput(),
						status = HttpStatusCode.OK
					)
				}
			}
		}
	}
}