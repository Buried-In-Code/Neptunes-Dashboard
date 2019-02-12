package macro.neptunes.history

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.contentType
import io.ktor.routing.get
import io.ktor.routing.route
import macro.neptunes.Util
import macro.neptunes.Router

/**
 * Created by Macro303 on 2019-Feb-08.
 */
object HistoryRouter: Router<Nothing>() {
	override fun getAll(): List<Nothing> = emptyList()
	override suspend fun get(call: ApplicationCall, useJson: Boolean): Nothing? = call.parseParam(useJson = useJson)

	override suspend fun ApplicationCall.parseParam(useJson: Boolean): Nothing? {
		notFound(useJson = useJson, type = "History", field = "", value = null)
		return null
	}

	override suspend fun ApplicationCall.parseBody(useJson: Boolean): Map<String, Any?>? {
		badRequest(useJson = useJson, fields = emptyArray(), values = emptyArray())
		return null
	}

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