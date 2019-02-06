package macro.neptunes.data

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.core.Util
import macro.neptunes.core.team.Team
import macro.neptunes.core.team.TeamHandler
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object TeamController {
	private val LOGGER = LogManager.getLogger(TeamController::class.java)

	fun getTeams(): List<Map<String, Any?>> {
		val teams = TeamHandler.teams.sortedBy { it.name }
		return teams.map { it.toJson() }
	}

	suspend fun ApplicationCall.parseTeam(): Team? {
		val name = parameters["Name"]
		val team = TeamHandler.teams.sortedBy { it.name }.firstOrNull {
			it.name.equals(name, ignoreCase = true)
		}
		if (name == null || team == null) {
			respond(
				message = Util.notFoundMessage(type = "Team", field = "Name", value = name),
				status = HttpStatusCode.NotFound
			)
			return null
		}
		return team
	}

	fun Route.teamRoutes() {
		route(path = "/teams") {
			contentType(contentType = ContentType.Application.Json) {
				get {
					call.respond(
						message = getTeams(),
						status = HttpStatusCode.OK
					)
				}
				post {
					call.respond(
						message = Util.notImplementedMessage(request = call.request),
						status = HttpStatusCode.NotImplemented
					)
				}
				route(path = "/{Name}") {
					get {
						val team = call.parseTeam() ?: return@get
						call.respond(
							message = team,
							status = HttpStatusCode.OK
						)
					}
					put {
						val team = call.parseTeam() ?: return@put
						call.respond(
							message = Util.notImplementedMessage(request = call.request),
							status = HttpStatusCode.NotImplemented
						)
					}
					delete {
						val team = call.parseTeam() ?: return@delete
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