package macro.neptunes.player

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.Util.JsonToMap
import macro.neptunes.player.PlayerTable.update
import macro.neptunes.team.TeamTable
import macro.neptunes.Router

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object PlayerRouter : Router<Player>() {
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

	override suspend fun ApplicationCall.parseBody(useJson: Boolean): Map<String, Any?>? {
		val body = this.receiveText().JsonToMap()
		val name = body["Name"] as String?
		var teamName = body["team"] as String?
		if (teamName != null) {
			teamName = TeamTable.selectCreate(name = teamName).name
		}
		return mapOf(
			"team" to teamName,
			"Name" to name
		)
	}

	fun Route.playerRoutes() {
		route(path = "/players") {
			contentType(contentType = ContentType.Application.Json) {
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
						val teamName = body["team"] as String? ?: player.teamName
						val name = body["Name"] as String? ?: player.name
						val valid = player.update(teamName = teamName, name = name)
						if (valid)
							call.respond(
								message = PlayerTable.select(alias = player.alias)!!.toOutput(showParent = true),
								status = HttpStatusCode.OK
							)
						else
							call.conflict(type = "player", field = "Name", value = name)
					}
				}
			}
		}
	}
}