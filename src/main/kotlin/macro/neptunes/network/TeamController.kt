package macro.neptunes.network

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.core.Team
import macro.neptunes.core.Util
import macro.neptunes.data.TeamTable
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object TeamController {
	private val LOGGER = LogManager.getLogger(TeamController::class.java)

	fun getTeams(): List<Team> {
		val teams = TeamTable.search().sorted()
		return teams
	}

	suspend fun ApplicationCall.parseTeam(useJson: Boolean = true): Team? {
		val name = parameters["Name"]
		val team = TeamTable.search(name = name ?: "").sorted().firstOrNull()
		if (name == null || team == null) {
			val message = Util.notFoundMessage(request = request, type = "Team", field = "Name", value = name)
			when (useJson) {
				true -> respond(
					message = message,
					status = HttpStatusCode.NotFound
				)
				false -> respond(
					message = FreeMarkerContent(template = "Exception.ftl", model = message),
					status = HttpStatusCode.NotFound
				)
			}
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