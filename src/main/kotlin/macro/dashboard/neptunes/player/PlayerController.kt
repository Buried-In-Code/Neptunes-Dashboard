package macro.dashboard.neptunes.player

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import macro.dashboard.neptunes.BadRequestException
import macro.dashboard.neptunes.NotFoundException
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.PlayerTable.update
import macro.dashboard.neptunes.team.TeamTable
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object PlayerController {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	fun Route.playerRoutes() {
		fun ApplicationCall.getGame(): Game {
			val gameID = parameters["game-ID"]?.toLongOrNull()
			return GameTable.select(ID = gameID ?: -1)
				?: throw NotFoundException(message = "No Game was found with the given ID '$gameID'")
		}
		route(path = "/{game-ID}/players") {
			get {
				val alias = call.request.queryParameters["alias"] ?: ""
				val players = PlayerTable.searchByGame(gameID = call.getGame().ID).filter { it.alias.contains(alias, ignoreCase = true) }
				call.respond(
					message = players.map { it.toOutput(showGame = false, showTeam = false, showTurns = false) },
					status = HttpStatusCode.OK
				)
			}
		}
		route(path = "/players/{player-ID}") {
			fun ApplicationCall.getPlayer(): Player {
				val playerID = parameters["player-ID"]?.toIntOrNull()
				return PlayerTable.select(ID = playerID ?: -1)
					?: throw NotFoundException(message = "No Player was found with the given ID '$playerID'")
			}
			get {
				call.respond(
					message = call.getPlayer().toOutput(showGame = true, showTeam = true),
					status = HttpStatusCode.OK
				)
			}
			put {
				val request = call.receiveOrNull<PlayerRequest>()
					?: throw BadRequestException(message = "A body is required")
				if (request.name == "")
					throw BadRequestException(message = "name is required")
				var player = call.getPlayer()
				val team = if (!request.team.isNullOrBlank())
					TeamTable.select(gameID = player.gameID, name = request.team)
				else
					null
				player.update(name = request.name, teamID = team?.ID ?: player.teamID)
				player = PlayerTable.select(ID = player.ID)
					?: throw NotFoundException(message = "No Player was found with the given ID '${player.ID}'")
				call.respond(
					message = player.toOutput(showGame = true, showTeam = true),
					status = HttpStatusCode.OK
				)
			}
		}
	}
}

class PlayerRequest(val name: String, val team: String?)