package macro.neptunes.team

import com.google.gson.JsonSyntaxException
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.DataExistsException
import macro.neptunes.IRouter
import macro.neptunes.NotImplementedException
import macro.neptunes.TeamRequest
import macro.neptunes.TeamRequest.Companion.JsonToRequest
import macro.neptunes.player.PlayerTable
import macro.neptunes.player.PlayerTable.update
import macro.neptunes.team.TeamTable.update

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object TeamRouter : IRouter<Team> {
	override fun getAll(): List<Team> = TeamTable.search().sorted()
	override suspend fun get(call: ApplicationCall, useJson: Boolean): Team? = call.parseParam(useJson = useJson)

	override suspend fun ApplicationCall.parseParam(useJson: Boolean): Team? {
		val name = parameters["Name"]
		val team = TeamTable.select(name = name ?: "Free For All")
		if (name == null || team == null) {
			notFound(useJson = useJson, type = "team", field = "Name", value = name)
			return null
		}
		return team
	}

	override suspend fun ApplicationCall.parseBody(useJson: Boolean): TeamRequest? {
		return try {
			this.receiveText().JsonToRequest()
		} catch (jse: JsonSyntaxException) {
			null
		}
	}

	fun Route.teamRoutes() {
		route(path = "/teams") {
			get {
				call.respond(
					message = getAll().map { it.toOutput() },
					status = HttpStatusCode.OK
				)
			}
			post {
				val body = call.parseBody() ?: return@post
				if (body.name == null) {
					call.badRequest(fields = arrayOf("Name"), values = arrayOf(body.name))
					return@post
				}
				val valid = TeamTable.insert(name = body.name)
				if (valid) {
					if (!body.players.isNullOrEmpty())
						body.players.forEach {
							val player = PlayerTable.select(alias = it)
							player?.update(
								teamName = body.name
							)
						}
					call.respond(
						message = TeamTable.select(name = body.name)!!.toOutput(showParent = true),
						status = HttpStatusCode.Created
					)
				} else
					throw DataExistsException(field = "Name", value = body.name)
			}
			route(path = "/{Name}") {
				get {
					val team = call.parseParam() ?: return@get
					call.respond(
						message = team.toOutput(showParent = true),
						status = HttpStatusCode.OK
					)
				}
				put {
					val team = call.parseParam() ?: return@put
					val body = call.parseBody() ?: return@put
					if (body.name == null) {
						call.badRequest(fields = arrayOf("Name"), values = arrayOf(body.name))
						return@put
					}
					val valid = team.update(name = body.name)
					if (valid) {
						if (!body.players.isNullOrEmpty())
							body.players.forEach {
								val player = PlayerTable.select(alias = it)
								player?.update(
									teamName = body.name
								)
							}
						call.respond(
							message = TeamTable.select(name = body.name)!!.toOutput(showParent = true),
							status = HttpStatusCode.OK
						)
					} else
						throw DataExistsException(field = "Name", value = body.name)
				}
				delete {
					val team = call.parseParam() ?: return@delete
					throw NotImplementedException()
				}
			}
		}
	}
}