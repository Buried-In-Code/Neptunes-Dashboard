package macro.neptunes.player

import com.google.gson.JsonSyntaxException
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import macro.neptunes.IRouter
import macro.neptunes.PlayerRequest
import macro.neptunes.PlayerRequest.Companion.JsonToRequest
import macro.neptunes.DataExistsException
import macro.neptunes.player.PlayerTable.update

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object PlayerRouter : IRouter<Player> {
	override fun getAll(): List<Player> = PlayerTable.search().sorted()
	override suspend fun get(call: ApplicationCall, useJson: Boolean): Player? = call.parseParam(useJson = useJson)

	override suspend fun ApplicationCall.parseParam(useJson: Boolean): Player? {
		val alias = parameters["Alias"]
		val player = PlayerTable.select(alias = alias ?: "INVALID")
		if (alias == null || player == null) {
			notFound(useJson = useJson, type = "player", field = "Alias", value = alias)
			return null
		}
		return player
	}

	override suspend fun ApplicationCall.parseBody(useJson: Boolean): PlayerRequest? {
		return try {
			this.receiveText().JsonToRequest()
		} catch (jse: JsonSyntaxException) {
			null
		}
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
					val player = call.parseParam() ?: return@get
					call.respond(
						message = player.toOutput(showParent = true),
						status = HttpStatusCode.OK
					)
				}
				put {
					val player = call.parseParam() ?: return@put
					val body = call.parseBody() ?: return@put
					val valid = player.update(teamName = body.getTeam()?.name ?: player.teamName, name = body.name ?: player.name)
					if (valid)
						call.respond(
							message = PlayerTable.select(alias = player.alias)!!.toOutput(showParent = true),
							status = HttpStatusCode.OK
						)
					else
						throw DataExistsException(field = "Name", value = body.name ?: player.name)
				}
			}
		}
	}
}