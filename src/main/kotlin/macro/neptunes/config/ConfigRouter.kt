package macro.neptunes.config

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import macro.neptunes.DataNotFoundException
import macro.neptunes.IRequest
import macro.neptunes.IRouter
import macro.neptunes.config.Config.Companion.CONFIG
import macro.neptunes.NotImplementedException

/**
 * Created by Macro303 on 2019-Jan-25.
 */
internal object ConfigRouter : IRouter<Config> {
	override fun getAll(): List<Config> = emptyList()
	override suspend fun get(call: ApplicationCall): Config = call.parseParam()

	override suspend fun ApplicationCall.parseParam(): Config {
		throw DataNotFoundException(type = "Config", field = "", value = null)
	}

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