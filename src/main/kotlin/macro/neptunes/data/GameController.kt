package macro.neptunes.data

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.Server
import macro.neptunes.core.game.GameHandler
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object GameController {
	private val LOGGER = LogManager.getLogger(GameController::class.java)

	fun Route.gameRoutes() {
		route(path = "/game") {
			contentType(contentType = ContentType.Application.Json) {
				get {
					call.respond(
						message = GameHandler.game,
						status = HttpStatusCode.OK
					)
				}
			}
		}
		route(path = "/refresh") {
			contentType(contentType = ContentType.Application.Json) {
				put {
					Server.refreshData()
					call.respond(
						message = "",
						status = HttpStatusCode.NoContent
					)
				}
			}
		}
	}
}