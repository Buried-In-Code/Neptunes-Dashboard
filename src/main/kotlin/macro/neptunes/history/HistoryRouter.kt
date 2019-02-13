package macro.neptunes.history

import io.ktor.application.ApplicationCall
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import macro.neptunes.IRequest
import macro.neptunes.IRouter
import macro.neptunes.NotImplementedException

/**
 * Created by Macro303 on 2019-Feb-08.
 */
internal object HistoryRouter : IRouter<Nothing> {
	override fun getAll(): List<Nothing> = emptyList()
	override suspend fun get(call: ApplicationCall, useJson: Boolean): Nothing? = call.parseParam(useJson = useJson)

	override suspend fun ApplicationCall.parseParam(useJson: Boolean): Nothing? {
		notFound(useJson = useJson, type = "History", field = "", value = null)
		return null
	}

	override suspend fun ApplicationCall.parseBody(useJson: Boolean): IRequest? {
		badRequest(useJson = useJson, fields = emptyArray(), values = emptyArray())
		return null
	}

	fun Route.historyRoutes() {
		route(path = "/history") {
			get {
				throw NotImplementedException()
			}
		}
	}
}