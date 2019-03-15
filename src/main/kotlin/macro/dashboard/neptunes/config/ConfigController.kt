package macro.dashboard.neptunes.config

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import macro.dashboard.neptunes.NotImplementedException
import macro.dashboard.neptunes.config.Config.Companion.CONFIG
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2019-Jan-25.
 */
internal object ConfigController {
	private val LOGGER = LogManager.getLogger()

	fun Route.settingRoutes() {
		route(path = "/settings") {
			get {
				call.respond(
					message = CONFIG,
					status = HttpStatusCode.OK
				)
			}
			put {
				throw NotImplementedException()
			}
		}
	}
}