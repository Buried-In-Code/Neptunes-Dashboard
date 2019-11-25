package macro.dashboard.neptunes.cycle

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import macro.dashboard.neptunes.BadRequestException
import macro.dashboard.neptunes.NotFoundException
import org.slf4j.LoggerFactory

/**
 * Last Updated by Macro303 on 2019-May-10
 */
object CycleRouter {
	private val LOGGER = LoggerFactory.getLogger(CycleRouter::class.java)

	private fun ApplicationCall.getPlayerID() = parameters["player-id"]?.toIntOrNull()
		?: throw BadRequestException("Invalid Player ID")

	private fun ApplicationCall.getCycle() = parameters["cycle"]?.toIntOrNull()
		?: throw BadRequestException("Invalid Cycle")

	internal fun getCycles(route: Route) {
		route.get {
			val playerID = call.getPlayerID()
			val cycles = CycleTable.searchByPlayer(playerID = playerID)
			call.respond(
				message = cycles.map { it.toMap() },
				status = HttpStatusCode.OK
			)
		}
	}

	internal fun getCycle(route: Route) {
		route.get {
			val playerID = call.getPlayerID()
			val cycleParam = call.getCycle()
			val cycle = CycleTable.select(playerID = playerID, cycle = cycleParam)
				?: throw NotFoundException("No Cycle Found with Player ID => $playerID at Cycle => $cycleParam")
			call.respond(
				message = cycle.toMap(),
				status = HttpStatusCode.OK
			)
		}
	}
}