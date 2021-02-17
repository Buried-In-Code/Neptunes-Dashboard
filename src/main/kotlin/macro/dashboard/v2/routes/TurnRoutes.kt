package macro.dashboard.v2.routes

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.BadRequestException
import io.ktor.features.NotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import macro.dashboard.Utils
import macro.dashboard.v2.schemas.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * Created by Macro303 on 2021-Feb-17
 */
object TurnRoutes {
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

	private fun ApplicationCall.getTurn(): Turn {
		val player = getPlayer()
		val turn = parameters["Turn"]?.toLongOrNull() ?: throw BadRequestException("Invalid Turn")
		return Turn.find {
			TurnTable.playerCol eq player.id and
					(TurnTable.turnCol eq turn)
		}.limit(1).firstOrNull() ?: throw NotFoundException("Unable to find Turn: `$turn`")
	}

	internal fun Route.turnRoutes() = route(path = "/turns") {
		get {
			call.listTurns()
		}
		route(path = "/{Turn}") {
			get {
				call.selectTurn()
			}
		}
	}

	private suspend fun ApplicationCall.listTurns(): Unit = newSuspendedTransaction(db = Utils.DATABASE) {
		val player = getPlayer()
		respond(
			message = player.turns.map { it.toJson() },
			status = HttpStatusCode.OK
		)
	}

	private suspend fun ApplicationCall.selectTurn(): Unit = newSuspendedTransaction(db = Utils.DATABASE) {
		val turn = getTurn()
		respond(
			message = turn.toJson(),
			status = HttpStatusCode.OK
		)
	}
}