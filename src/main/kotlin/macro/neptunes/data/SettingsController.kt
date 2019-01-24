package macro.neptunes.data

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.core.Config.Companion.CONFIG
import macro.neptunes.core.Util
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2019-Jan-25.
 */
object SettingsController {
	private val LOGGER = LogManager.getLogger(SettingsController::class.java)

	fun Route.settingRoutes() {
		route(path = "/settings") {
			contentType(contentType = ContentType.Application.Json) {
				get {
					call.respond(
						message = CONFIG,
						status = HttpStatusCode.OK
					)
				}
				put {
					call.respond(
						message = Util.notImplementedMessage(request = call.request),
						status = HttpStatusCode.NotImplemented
					)
				}
			}
		}
	}
}