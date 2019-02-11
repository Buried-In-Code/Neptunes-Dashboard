package macro.neptunes.network

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.Server
import macro.neptunes.core.Config.Companion.CONFIG
import macro.neptunes.data.GameTable
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
						message = GameTable.select(ID = CONFIG.gameID) ?: emptyMap<String, String>(),
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
						message = emptyMap<String, String>(),
						status = HttpStatusCode.NoContent
					)
				}
			}
		}
	}
}