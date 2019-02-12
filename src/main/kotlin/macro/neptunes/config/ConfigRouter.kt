package macro.neptunes.config

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.config.Config.Companion.CONFIG
import macro.neptunes.Router
import macro.neptunes.Util

/**
 * Created by Macro303 on 2019-Jan-25.
 */
object ConfigRouter: Router<Nothing>() {
	override fun getAll(): List<Nothing> = emptyList()
	override suspend fun get(call: ApplicationCall, useJson: Boolean): Nothing? = call.parseParam(useJson = useJson)

	override suspend fun ApplicationCall.parseParam(useJson: Boolean): Nothing? {
		notFound(useJson = useJson, type = "Config", field = "", value = null)
		return null
	}

	override suspend fun ApplicationCall.parseBody(useJson: Boolean): Map<String, Any?>? {
		badRequest(useJson = useJson, fields = emptyArray(), values = emptyArray())
		return null
	}

	fun Route.settingRoutes() {
		route(path = "/config") {
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