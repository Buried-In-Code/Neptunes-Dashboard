package macro.neptunes.data

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.core.Util
import macro.neptunes.core.team.Team
import macro.neptunes.core.team.TeamHandler
import org.apache.logging.log4j.LogManager
import java.util.stream.Collectors

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object TeamController {
	private val LOGGER = LogManager.getLogger(TeamController::class.java)

	private fun sortTeams(sort: String): List<Team> {
		return when (sort.toLowerCase()) {
			"name" -> TeamHandler.sortByName()
			"stars" -> TeamHandler.sortByStars()
			"ships" -> TeamHandler.sortByShips()
			"economy" -> TeamHandler.sortByEconomy()
			"industry" -> TeamHandler.sortByIndustry()
			"science" -> TeamHandler.sortByScience()
			else -> TeamHandler.teams
		}
	}

	private fun filterTeams(filterString: String, teams: List<Team>): List<Team> {
		val filter: Map<String, String> = filterString.trim()
			.split(",")
			.stream()
			.map { it.split(":") }
			.collect(Collectors.toMap({ it[0].toLowerCase() }, { if (it.size > 1) it[1] else "" }))
		val name: String = filter["name"] ?: ""
		val playerName: String = filter["player-name"] ?: ""
		val playerAlias: String = filter["player-alias"] ?: ""
		return TeamHandler.filter(
			name = name,
			playerName = playerName,
			playerAlias = playerAlias,
			teams = teams
		)
	}

	fun Route.teamRoutes() {
		route(path = "/teams") {
			contentType(contentType = ContentType.Application.Json) {
				get {
					call.respond(
						message = TeamHandler.teams,
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
						val name = call.parameters["Name"] ?: "Unknown"
						val team = TeamHandler.teams.sorted().firstOrNull { it.name.equals(name, ignoreCase = true) }
						if (team == null)
							call.respond(
								message = Message(
									title = "No Team Found",
									content = "No Team was found with the Name: $name"
								),
								status = HttpStatusCode.NotFound
							)
						else
							call.respond(
								message = team,
								status = HttpStatusCode.OK
							)
					}
					put {
						call.respond(
							message = Util.notImplementedMessage(request = call.request),
							status = HttpStatusCode.NotImplemented
						)
					}
					delete {
						call.respond(
							message = Util.notImplementedMessage(request = call.request),
							status = HttpStatusCode.NotImplemented
						)
					}
					route(path = "/players"){
						get{
							val name = call.parameters["Name"] ?: "Unknown"
							val team = TeamHandler.teams.sorted().firstOrNull { it.name.equals(name, ignoreCase = true) }
							if (team == null)
								call.respond(
									message = Message(
										title = "No Team Found",
										content = "No Team was found with the Name: $name"
									),
									status = HttpStatusCode.NotFound
								)
							else
								call.respond(
									message = team.members.sorted(),
									status = HttpStatusCode.OK
								)
						}
					}
				}
				get(path = "/leaderboard") {
					call.respond(
						message = TeamHandler.teams,
						status = HttpStatusCode.OK
					)
				}
			}
		}
	}
}