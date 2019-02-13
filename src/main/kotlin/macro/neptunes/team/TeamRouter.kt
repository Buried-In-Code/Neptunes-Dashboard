package macro.neptunes.team

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.*
import macro.neptunes.player.PlayerTable
import macro.neptunes.player.PlayerTable.update
import macro.neptunes.team.TeamTable.update

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object TeamRouter : IRouter<Team> {
	override fun getAll(): List<Team> = TeamTable.search().sorted()
	override suspend fun get(call: ApplicationCall): Team = call.parseParam()

	override suspend fun ApplicationCall.parseParam(): Team {
		val name = parameters["Name"]
		val team = TeamTable.select(name = name ?: "Free For All")
		if (name == null || team == null)
			throw DataNotFoundException(type = "Team", field = "Name", value = name)
		return team
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
				val body = call.receive<TeamRequest>()
				body.name ?: throw InvalidBodyException(field = "Name")
				val valid = TeamTable.insert(name = body.name)
				if (valid) {
					body.players.forEach {
						PlayerTable.select(alias = it)?.update(
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
					val team = call.parseParam()
					call.respond(
						message = team.toOutput(showParent = true),
						status = HttpStatusCode.OK
					)
				}
				put {
					val team = call.parseParam()
					val body = call.receive<TeamRequest>()
					val valid = team.update(name = body.name ?: team.name)
					if (valid) {
						body.players.forEach {
							PlayerTable.select(alias = it)?.update(
								teamName = body.name ?: team.name
							)
						}
						call.respond(
							message = TeamTable.select(name = body.name ?: team.name)!!.toOutput(showParent = true),
							status = HttpStatusCode.OK
						)
					} else
						throw DataExistsException(field = "Name", value = body.name ?: team.name)
				}
				delete {
					val team = call.parseParam()
					throw NotImplementedException()
				}
			}
		}
	}
}