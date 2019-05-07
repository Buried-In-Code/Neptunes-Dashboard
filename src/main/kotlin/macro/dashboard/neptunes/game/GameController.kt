package macro.dashboard.neptunes.game

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import macro.dashboard.neptunes.BadRequestException
import macro.dashboard.neptunes.Config.Companion.CONFIG
import macro.dashboard.neptunes.NotFoundException
import macro.dashboard.neptunes.backend.Proteus
import macro.dashboard.neptunes.backend.Triton
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object GameController {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	fun Route.gameRoutes() {
		route(path = "/games") {
			get {
				val games = GameTable.searchAll()
				call.respond(
					message = games.map { it.toOutput() },
					status = HttpStatusCode.OK
				)
			}
			get(path = "/latest") {
				val game = GameTable.selectLatest()
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
						?: throw BadRequestException(message = "No Game was found with the given ID '$param'")
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
					when (game.gameType) {
						"Triton" -> Triton.getGame(gameID = game.ID, code = CONFIG.games[param] ?: "")
						"Proteus" -> Proteus.getGame(gameID = game.ID, code = CONFIG.games[param] ?: "")
					}
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