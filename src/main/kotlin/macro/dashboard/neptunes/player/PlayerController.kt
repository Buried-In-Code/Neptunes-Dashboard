package macro.dashboard.neptunes.player

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import macro.dashboard.neptunes.DataNotFoundException
import macro.dashboard.neptunes.player.PlayerTable.update

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object PlayerController {
	fun getAll(): List<Player> = PlayerTable.search().sorted()
	fun get(call: ApplicationCall): Player = call.parseParam()

	fun ApplicationCall.parseParam(): Player {
		val alias = parameters["Alias"]
		val player = PlayerTable.select(alias = alias ?: "INVALID")
		if (alias == null || player == null)
			throw DataNotFoundException(type = "Player", field = "Alias", value = alias)
		return player
	}

	fun Route.playerRoutes() {
		route(path = "/players") {
			get {
				call.respond(
					message = getAll().map { it.toOutput() },
					status = HttpStatusCode.OK
				)
			}
			route(path = "/{Alias}") {
				get {
					val player = call.parseParam()
					call.respond(
						message = player.toOutput(showParent = true),
						status = HttpStatusCode.OK
					)
				}
				put {
					val player = call.parseParam()
					val body = call.receive<PlayerRequest>()
					val updated = player.update(
						name = body.name ?: player.name
					)
					call.respond(
						message = updated.toOutput(showParent = true),
						status = HttpStatusCode.OK
					)

				}
			}
		}
	}
}

data class PlayerRequest(val name: String?)