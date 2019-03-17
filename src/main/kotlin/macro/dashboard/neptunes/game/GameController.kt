package macro.dashboard.neptunes.game

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import macro.dashboard.neptunes.BadRequestException
import macro.dashboard.neptunes.NotFoundException
import macro.dashboard.neptunes.backend.Neptunes
import macro.dashboard.neptunes.config.Config.Companion.CONFIG
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
					Neptunes.getGame(gameID = game.ID, code = CONFIG.games[game.ID] ?: "")
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