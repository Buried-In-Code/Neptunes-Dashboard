package macro.dashboard.neptunes.player

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import macro.dashboard.neptunes.BadRequestResponse
import macro.dashboard.neptunes.NotFoundResponse
import macro.dashboard.neptunes.NotYetImplementedResponse
import org.slf4j.LoggerFactory

/**
 * Last Updated by Macro303 on 2019-May-10
 */
object PlayerRouter {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	private fun ApplicationCall.getPlayerID() = parameters["player-id"]?.toIntOrNull()
		?: throw BadRequestResponse("Invalid Player ID")

	private fun ApplicationCall.getTeamQuery() = request.queryParameters["team"] ?: "%"
	private fun ApplicationCall.getNameQuery() = request.queryParameters["name"] ?: "%"
	private fun ApplicationCall.getAliasQuery() = request.queryParameters["alias"] ?: "%"

	internal fun getPlayers(route: Route) {
		route.get {
			val team = call.getTeamQuery()
			val name = call.getNameQuery()
			val alias = call.getAliasQuery()
			val players = PlayerTable.search(team = team, name = name, alias = alias)
			call.respond(
				message = players.map { it.toMap() },
				status = HttpStatusCode.OK
			)
		}
	}

	internal fun getPlayer(route: Route) {
		route.get {
			val playerID = call.getPlayerID()
			val player = PlayerTable.select(ID = playerID) ?: throw NotFoundResponse("No Player with ID => $playerID")
			call.respond(
				message = player.toMap(),
				status = HttpStatusCode.OK
			)
		}
	}

	internal fun updatePlayer(route: Route) {
		route.put {
			val playerID = call.getPlayerID()
			throw NotYetImplementedResponse()
		}
	}
}