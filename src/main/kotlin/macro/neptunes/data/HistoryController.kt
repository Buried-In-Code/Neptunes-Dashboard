package macro.neptunes.data

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.contentType
import io.ktor.routing.get
import io.ktor.routing.route
import macro.neptunes.core.History
import macro.neptunes.core.Util
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2019-Feb-08.
 */
object HistoryController {
	private val LOGGER = LogManager.getLogger(HistoryController::class.java)
	val games = ArrayList<History>()

	fun Route.historyRoutes() {
		route(path = "/history") {
			contentType(contentType = ContentType.Application.Json) {
				get {
					call.respond(
						message = Util.notImplementedMessage(request = call.request),
						status = HttpStatusCode.NotImplemented
					)
				}
			}
		}
	}
}