package macro.dashboard.neptunes.team

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import macro.dashboard.neptunes.DataNotFoundException
import macro.dashboard.neptunes.InvalidBodyException
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.player.PlayerTable.update

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object TeamController {
	fun getAll(): List<Team> = TeamTable.search().sorted()
	fun get(call: ApplicationCall): Team = call.parseParam()

	fun ApplicationCall.parseParam(): Team {
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
				val created = TeamTable.insert(name = body.name)
				body.players.forEach {
					PlayerTable.select(game = created.getGame(), alias = it)?.update(teamName = created.name)
				}
				call.respond(
					message = "",
					status = HttpStatusCode.NoContent
				)
			}
			route(path = "/{Name}") {
				get {
					val team = call.parseParam()
					call.respond(
						message = team.toOutput(showParent = true),
						status = HttpStatusCode.OK
					)
				}
			}
		}
	}
}

data class TeamRequest(val name: String?, val players: List<String> = emptyList())