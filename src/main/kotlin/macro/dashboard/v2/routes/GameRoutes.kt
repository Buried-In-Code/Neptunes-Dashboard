package macro.dashboard.v2.routes

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.BadRequestException
import io.ktor.features.NotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.*
import macro.dashboard.Utils
import macro.dashboard.Utils.lowerCase
import macro.dashboard.v2.external.Triton
import macro.dashboard.v2.requests.NewGameRequest
import macro.dashboard.v2.routes.PlayerRoutes.playerRoutes
import macro.dashboard.v2.schemas.Game
import macro.dashboard.v2.schemas.GameTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * Created by Macro303 on 2021-Feb-16
 */
object GameRoutes {
	private fun ApplicationCall.getGame(): Game {
		val gameId = parameters["Game ID"]?.toLongOrNull()
			?: return Game.all().orderBy(GameTable.startTimeCol to SortOrder.DESC).limit(1).firstOrNull()
				?: throw NotFoundException("Unable to find game")
		return Game.findById(gameId) ?: throw BadRequestException("Unable to find Game with ID: `$gameId`")
	}

	internal fun Route.gameRoutes() = route(path = "/games") {
		get {
			call.listGames()
		}
		route(path = "/{Game ID}") {
			get {
				call.selectGame()
			}
			post {
				call.addGame()
			}
			put {
				call.updateGame()
			}
			playerRoutes()
		}
	}

	private suspend fun ApplicationCall.listGames(): Unit = newSuspendedTransaction(db = Utils.DATABASE) {
		val query = parameters.lowerCase()
		respond(
			message = Game.all().orderBy(GameTable.startTimeCol to SortOrder.DESC).map {
				it.toJson()
			},
			status = HttpStatusCode.OK
		)
	}

	private suspend fun ApplicationCall.selectGame(): Unit = newSuspendedTransaction(db = Utils.DATABASE) {
		respond(
			message = getGame().toJson(),
			status = HttpStatusCode.OK
		)
	}

	private suspend fun ApplicationCall.addGame(): Unit = newSuspendedTransaction(db = Utils.DATABASE) {
		val gameNumber = parameters["Game ID"]?.toLongOrNull()
			?: throw BadRequestException("Invalid Game ID: `${parameters["Game ID"]}`")
		val gameRequest = receiveOrNull<NewGameRequest>() ?: throw BadRequestException("Invalid request body")
		gameRequest.validate()
		var game = Game.findById(gameNumber)
		if (game != null)
			respond(
				message = "Game with ID already Exists",
				status = HttpStatusCode.Conflict
			)
		else {
			Triton.getGame(gameNumber, gameRequest.apiCode, gameRequest.tickRate)
			game = Game.findById(gameNumber) ?: throw NotFoundException("Unable to find Game with ID: `$gameNumber`")
			respond(
				message = game.toJson(),
				status = HttpStatusCode.Created
			)
		}
	}

	private suspend fun ApplicationCall.updateGame(): Unit = newSuspendedTransaction(db = Utils.DATABASE) {
		var game = getGame()
		Triton.getGame(game.id.value, game.apiCode, game.tickRate)
		game = Game.findById(game.id) ?: throw NotFoundException("Unable to find Game with ID: `${game.id.value}`")
		respond(
			message = game.toJson(),
			status = HttpStatusCode.Accepted
		)
	}
}