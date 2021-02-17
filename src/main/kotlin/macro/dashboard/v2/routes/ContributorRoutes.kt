package macro.dashboard.v2.routes

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import macro.dashboard.Utils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * Created by Macro303 on 2021-Feb-17
 */
object ContributorRoutes {
	internal fun Route.contributorRoutes() = route(path = "/contributors") {
		get {
			call.listContributors()
		}
	}

	private suspend fun ApplicationCall.listContributors(): Unit = newSuspendedTransaction(db = Utils.DATABASE) {
		respond(
			message = listOf(
				mapOf<String, Any?>(
					"Title" to "Macro303",
					"Role" to "Creator and Maintainer"
				).toSortedMap(),
				mapOf<String, Any?>(
					"Title" to "Img Bot App",
					"Role" to "Image Processor"
				).toSortedMap(),
				mapOf<String, Any?>(
					"Title" to "Dependabot App",
					"Role" to "Automated dependency updates"
				).toSortedMap()
			),
			status = HttpStatusCode.OK
		)
	}
}