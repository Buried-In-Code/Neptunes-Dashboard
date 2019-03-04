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
import macro.dashboard.neptunes.InvalidBodyException
import macro.dashboard.neptunes.player.PlayerTable.update

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object PlayerController {
	fun get(call: ApplicationCall): Player = call.parseParam()

	private fun ApplicationCall.parseParam(): Player {
		val ID = parameters["ID"]
		val player = PlayerTable.select(ID = ID?.toIntOrNull() ?: -1)
		if (ID == null || player == null)
			throw DataNotFoundException(type = "Player", field = "ID", value = ID)
		return player
	}

	fun Route.playerRoutes() {
		route(path = "/players") {
			get {
				val alias = call.request.queryParameters["alias"] ?: ""
				call.respond(
					message = PlayerTable.search(alias = alias).map { it.toOutput() },
					status = HttpStatusCode.OK
				)
			}
			route(path = "/{ID}") {
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
					player.update(
						name = body.name
					)
					call.respond(
						message = "",
						status = HttpStatusCode.OK
					)

				}
			}
		}
	}
}

class PlayerRequest(name: String?){
	val name: String = name ?: throw InvalidBodyException(field = "name")
}