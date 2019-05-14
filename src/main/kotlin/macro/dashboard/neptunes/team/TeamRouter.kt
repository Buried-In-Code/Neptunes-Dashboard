package macro.dashboard.neptunes.team

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import macro.dashboard.neptunes.BadRequestResponse
import macro.dashboard.neptunes.NotFoundResponse
import macro.dashboard.neptunes.NotYetImplementedResponse
import org.slf4j.LoggerFactory

/**
 * Last Updated by Macro303 on 2019-May-13
 */
object TeamRouter {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	private fun ApplicationCall.getTeamID() = parameters["team-id"]?.toIntOrNull()
		?: throw BadRequestResponse("Invalid Team ID")

	private fun ApplicationCall.getNameQuery() = request.queryParameters["name"] ?: "%"

	internal fun getTeams(route: Route) {
		route.get {
			val name = call.getNameQuery()
			val teams = TeamTable.search(name = name)
			call.respond(
				message = teams.map { it.toMap() },
				status = HttpStatusCode.OK
			)
		}
	}

	internal fun addTeam(route: Route) {
		route.post {
			throw NotYetImplementedResponse()
		}
	}

	internal fun getTeam(route: Route) {
		route.get {
			val teamID = call.getTeamID()
			val team = TeamTable.select(ID = teamID) ?: throw NotFoundResponse("No Team with ID => $teamID")
			call.respond(
				message = team.toMap(),
				status = HttpStatusCode.OK
			)
		}
	}

	internal fun updateTeam(route: Route) {
		route.put {
			val teamID = call.getTeamID()
			throw NotYetImplementedResponse()
		}
	}
}