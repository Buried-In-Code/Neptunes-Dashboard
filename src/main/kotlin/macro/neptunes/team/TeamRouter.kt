package macro.neptunes.team

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.Util
import macro.neptunes.Util.JsonToMap
import macro.neptunes.team.TeamTable.update
import macro.neptunes.Router

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object TeamRouter : Router<Team>() {
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

	override suspend fun ApplicationCall.parseBody(useJson: Boolean): Map<String, Any>? {
		val body = this.receiveText().JsonToMap()
		val name = body["Name"] as String?
		if (name == null) {
			badRequest(useJson = useJson, fields = arrayOf("Name"), values = arrayOf(name))
			return null
		}
		return mapOf(
			"Name" to name
		)
	}

	fun Route.teamRoutes() {
		route(path = "/teams") {
			contentType(contentType = ContentType.Application.Json) {
				get {
					call.respond(
						message = getAll().map { it.toOutput() },
						status = HttpStatusCode.OK
					)
				}
				post {
					val body = call.parseBody() ?: return@post
					val name = body["Name"] as String
					val valid = TeamTable.insert(name = name)
					if (valid)
						call.respond(
							message = TeamTable.select(name = name)!!.toOutput(showParent = true),
							status = HttpStatusCode.Created
						)
					else
						call.conflict(type = "team", field = "Name", value = name)
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
						val name = body["Name"] as String
						val valid = team.update(name = name)
						if (valid)
							call.respond(
								message = TeamTable.select(name = name)!!.toOutput(showParent = true),
								status = HttpStatusCode.OK
							)
						else
							call.conflict(type = "team", field = "Name", value = name)
					}
					delete {
						val team = call.parseParam() ?: return@delete
						call.respond(
							message = Util.notImplementedMessage(request = call.request),
							status = HttpStatusCode.NotImplemented
						)
					}
				}
			}
		}
	}
}