package macro.neptunes.core

import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.contentType
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.patch
import io.ktor.routing.route
import macro.neptunes.Application
import macro.neptunes.core.Config.Companion.CONFIG
import macro.neptunes.core.toMap
import macro.neptunes.data.Message
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-23.
 */
object UtilController {
	private val LOGGER = LogManager.getLogger(UtilController::class.java)

	fun Route.util() {
		route("/settings") {
			get {
				if (call.request.contentType() == ContentType.Application.Json)
					call.respond(message = CONFIG.toMap())
				else
					call.respond(
						message = FreeMarkerContent(
							template = "message.ftl",
							model = mapOf("message" to Util.getNotImplementedMessage(endpoint = "/settings"))
						), status = HttpStatusCode.NotImplemented
					)
			}
			patch {
				if (call.request.contentType() == ContentType.Application.Json)
					call.respond(
						message = Util.getNotImplementedMessage(endpoint = "/settings"),
						status = HttpStatusCode.NotImplemented
					)
				else
					call.respond(
						message = FreeMarkerContent(
							template = "message.ftl",
							model = mapOf("message" to Util.getNotImplementedMessage(endpoint = "/settings"))
						), status = HttpStatusCode.NotImplemented
					)
			}
		}
		get("/refresh") {
			Application.refreshData()
			call.respond(status = HttpStatusCode.NoContent, message = Message(title = "No Content", content = ""))
		}
	}
}