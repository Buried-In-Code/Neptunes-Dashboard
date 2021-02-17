package macro.dashboard.v2.routes

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.BadRequestException
import io.ktor.features.NotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.patch
import io.ktor.routing.route
import macro.dashboard.Utils
import macro.dashboard.v2.requests.EditPlayerRequest
import macro.dashboard.v2.routes.TurnRoutes.turnRoutes
import macro.dashboard.v2.schemas.Game
import macro.dashboard.v2.schemas.GameTable
import macro.dashboard.v2.schemas.Player
import macro.dashboard.v2.schemas.PlayerTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * Created by Macro303 on 2021-Feb-16
 */
object PlayerRoutes {
	private fun ApplicationCall.getGame(): Game {
		val gameId = parameters["Game ID"]?.toLongOrNull()
			?: return Game.all().orderBy(GameTable.startTimeCol to SortOrder.DESC).limit(1).firstOrNull()
				?: throw NotFoundException("Unable to find game")
		return Game.findById(gameId) ?: throw BadRequestException("Unable to find Game with ID: `$gameId`")
	}

	private fun ApplicationCall.getPlayer(): Player {
		val game = getGame()
		val username = parameters["Username"] ?: throw BadRequestException("Invalid Player Username")
		return Player.find {
			PlayerTable.gameCol eq game.id and
					(PlayerTable.usernameCol eq username)
		}.limit(1).firstOrNull() ?: throw NotFoundException("Unable to find Player with Username: `$username`")
	}

	internal fun Route.playerRoutes() = route(path = "/players") {
		get {
			call.listPlayers()
		}
		route(path = "/{Username}") {
			get {
				call.selectPlayer()
			}
			patch {
				call.updatePlayer()
			}
			turnRoutes()
		}
	}

	private suspend fun ApplicationCall.listPlayers(): Unit = newSuspendedTransaction(db = Utils.DATABASE) {
		val game = getGame()
		respond(
			message = game.players.map { it.toJson() },
			status = HttpStatusCode.OK
		)
	}

	private suspend fun ApplicationCall.selectPlayer(): Unit = newSuspendedTransaction(db = Utils.DATABASE) {
		val player = getPlayer()
		respond(
			message = player.toJson(),
			status = HttpStatusCode.OK
		)
	}

	private suspend fun ApplicationCall.updatePlayer(): Unit = newSuspendedTransaction(db = Utils.DATABASE) {
		val player = getPlayer()
		val playerRequest = receiveOrNull<EditPlayerRequest>() ?: throw BadRequestException("Invalid request body")
		playerRequest.validate()
		player.team = playerRequest.team
		respond(
			message = player.toJson(),
			status = HttpStatusCode.Accepted
		)
	}
}