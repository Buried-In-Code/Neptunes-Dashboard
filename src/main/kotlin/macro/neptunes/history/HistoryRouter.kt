package macro.neptunes.history

import io.ktor.application.ApplicationCall
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import macro.neptunes.DataNotFoundException
import macro.neptunes.IRouter
import macro.neptunes.NotImplementedException

/**
 * Created by Macro303 on 2019-Feb-08.
 */
internal object HistoryRouter : IRouter<History> {
	override fun getAll(): List<History> = emptyList()
	override suspend fun get(call: ApplicationCall): History = call.parseParam()

	override suspend fun ApplicationCall.parseParam(): History {
		throw DataNotFoundException(type = "History", field = "", value = null)
	}

	fun Route.historyRoutes() {
		route(path = "/history") {
			get {
				throw NotImplementedException()
			}
		}
	}
}