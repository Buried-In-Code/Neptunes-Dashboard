package macro.neptunes.data.controllers

import io.ktor.application.call
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

/**
 * Created by Macro303 on 2018-Nov-23.
 */
object UtilController {

	fun Route.util() {
		route("/config") {
			get {
				if (call.request.contentType() == ContentType.Application.Json)
					call.respond(message = CONFIG.toMap())
				else
					call.respond(status = HttpStatusCode.NotImplemented, message = Message("Not Yet Implemented"))
			}
			patch {
				call.respond(status = HttpStatusCode.NotImplemented, message = Message("Not Yet Implemented"))
			}
		}
		get("/refresh") {
			Application.refreshData()
			call.respond(status = HttpStatusCode.NoContent, message = Message("No Content"))
		}
	}
}