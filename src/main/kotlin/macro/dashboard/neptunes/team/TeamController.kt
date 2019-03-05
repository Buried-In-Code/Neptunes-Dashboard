package macro.dashboard.neptunes.team

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import macro.dashboard.neptunes.DataNotFoundException
import macro.dashboard.neptunes.GeneralException
import macro.dashboard.neptunes.InvalidBodyException
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.player.PlayerTable.update
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object TeamController {
	private val LOGGER: Logger = LogManager.getLogger(TeamController::class.java)
	fun get(call: ApplicationCall): Team = call.parseParam()

	private fun ApplicationCall.parseParam(): Team {
		val ID = parameters["ID"]
		val team = TeamTable.select(ID = ID?.toIntOrNull() ?: -1)
		if (ID == null || team == null)
			throw DataNotFoundException(type = "Team", field = "ID", value = ID)
		return team
	}

	fun Route.teamRoutes() {
		route(path = "/teams") {
			get {
				val name = call.request.queryParameters["name"] ?: ""
				call.respond(
					message = TeamTable.search(name = name).map { it.toOutput(showGame = false, showPlayers = false) },
					status = HttpStatusCode.OK
				)
			}
			post {
				val body = call.receive<TeamRequest>()
				val created = TeamTable.insert(gameID = body.gameID, name = body.name)
				if (!created)
					LOGGER.info("Team ${body.name} already exists")
				val team = TeamTable.search(gameID = body.gameID, name = body.name).firstOrNull() ?: throw GeneralException()
				body.players.forEach { alias ->
					PlayerTable.search(gameID = body.gameID, alias = alias).firstOrNull()?.update(teamID = team.ID)
				}
				call.respond(
					message = "",
					status = HttpStatusCode.NoContent
				)
			}
			route(path = "/{ID}") {
				get {
					val team = call.parseParam()
					call.respond(
						message = team.toOutput(showGame = true, showPlayers = true),
						status = HttpStatusCode.OK
					)
				}
			}
		}
	}
}

class TeamRequest(gameID: Long?, name: String?, val players: List<String> = emptyList()){
	val gameID: Long = gameID ?: throw InvalidBodyException(field = "gameID")
	val name: String = name ?: throw InvalidBodyException(field = "name")

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as TeamRequest

		if (name != other.name) return false
		if (players != other.players) return false
		if (gameID != other.gameID) return false

		return true
	}

	override fun hashCode(): Int {
		var result = name?.hashCode() ?: 0
		result = 31 * result + players.hashCode()
		result = 31 * result + gameID.hashCode()
		return result
	}

	override fun toString(): String {
		return "TeamRequest(name=$name, players=$players, gameID=$gameID)"
	}
}