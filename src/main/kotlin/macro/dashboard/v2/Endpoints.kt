package macro.dashboard.v2

import io.ktor.routing.Route
import io.ktor.routing.route
import macro.dashboard.v2.routes.ContributorRoutes.contributorRoutes
import macro.dashboard.v2.routes.GameRoutes.gameRoutes

/**
 * Created by Macro303 on 2021-Feb-16
 */
object Endpoints {
	internal fun Route.v2Routes() = route(path = "/v2") {
		gameRoutes()
		contributorRoutes()
	}
}