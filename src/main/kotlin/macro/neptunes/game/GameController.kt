package macro.neptunes.game

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.DataNotFoundException
import macro.neptunes.GeneralException
import macro.neptunes.IRouter
import macro.neptunes.Util
import macro.neptunes.Util.JsonToMap
import macro.neptunes.backend.Neptunes
import macro.neptunes.backend.RESTClient
import macro.neptunes.config.Config.Companion.CONFIG
import org.apache.logging.log4j.LogManager
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object GameController : IRouter<Game> {
	private val LOGGER = LogManager.getLogger(GameController::class.java)
	override fun getAll(): List<Game> = listOfNotNull(GameTable.select())
	override suspend fun get(call: ApplicationCall): Game = call.parseParam()

	override suspend fun ApplicationCall.parseParam(): Game {
		val ID = parameters["ID"]
		val game = GameTable.select(ID = ID?.toLongOrNull() ?: 1.toLong())
		if (ID == null || game == null)
			throw DataNotFoundException(type = "Game", field = "ID", value = ID)
		return game
	}

	fun Route.gameRoutes() {
		route(path = "/game") {
			route(path = "/{ID}") {
				get {
					call.respond(
						message = get(game = call.parseParam()),
						status = HttpStatusCode.OK
					)
				}
				get(path = "/latest") {
					call.respond(
						message = getLatest(game = call.parseParam()),
						status = HttpStatusCode.OK
					)
				}
				post {
					val gameID = call.parameters["ID"]?.toLongOrNull()
						?: throw DataNotFoundException(type = "Game", field = "ID", value = call.parameters["ID"])
					val game = addGame(ID = gameID) ?: throw GeneralException()
					call.respond(
						message = game,
						status = HttpStatusCode.Created
					)
				}
			}
			get {
				call.respond(
					message = getLatest(),
					status = HttpStatusCode.OK
				)
			}
			put {
				addGame(ID = CONFIG.gameID)
				call.respond(
					message = emptyMap<String, String>(),
					status = HttpStatusCode.NoContent
				)
			}
		}
	}

	fun addGame(ID: Long): Map<String, Any>? {
		val client = RESTClient(endpointUrl = Util.ENDPOINT + ID)
		val response = client.getRequest(endpoint = "/basic")
		if (response["Code"] == 200) {
			val data = (response["Data"] as String).JsonToMap()
			val game = parseGame(ID = ID, response = data)
			var turn = GameTurnTable.selectLatest(ID = game.ID)
			val production = (data["productions"] as Double).roundToInt()
			if (turn == null || turn.production != production) {
				turn = parseTurn(game = game, response = data)
				Neptunes.updatePlayers()
			}
			return get(ID = game.ID)
		}
		return null
	}

	fun get(ID: Long = CONFIG.gameID, showParent: Boolean = false, showChildren: Boolean = true): Map<String, Any> {
		val game = GameTable.select(ID = ID) ?: throw DataNotFoundException(type = "Game", field = "ID", value = ID)
		return get(game = game, showParent = showParent, showChildren = showChildren)
	}

	fun get(game: Game, showParent: Boolean = false, showChildren: Boolean = true): Map<String, Any> {
		val turns = GameTurnTable.search(ID = game.ID)
		var output = mapOf(
			"ID" to game.ID,
			"name" to game.name,
			"totalStars" to game.totalStars,
			"victoryStars" to game.victoryStars,
			"productionRate" to game.productionRate,
			"turns" to turns.map {
				mapOf(
					"startTime" to it.startTime.format(Util.JAVA_FORMATTER),
					"production" to it.production,
					"isGameOver" to it.isGameOver,
					"isPaused" to it.isPaused,
					"isStarted" to it.isStarted,
					"tick" to it.tick
				).toSortedMap()
			}
		)
		if (showChildren) {
			output = output.plus("teams" to game.getTeams().map { it.toOutput(showParent = false) })
		}
		return output.toSortedMap()
	}

	fun getLatest(
		ID: Long = CONFIG.gameID,
		showParent: Boolean = false,
		showChildren: Boolean = true
	): Map<String, Any> {
		val game = GameTable.select(ID = ID) ?: throw DataNotFoundException(type = "Game", field = "ID", value = ID)
		return getLatest(game = game, showParent = showParent, showChildren = showChildren)
	}

	fun getLatest(game: Game, showParent: Boolean = false, showChildren: Boolean = true): Map<String, Any> {
		val turn = GameTurnTable.selectLatest(ID = game.ID)
			?: throw DataNotFoundException(type = "Game", field = "ID", value = game.ID)
		var output = mapOf(
			"ID" to game.ID,
			"name" to game.name,
			"totalStars" to game.totalStars,
			"victoryStars" to game.victoryStars,
			"productionRate" to game.productionRate,
			"startTime" to turn.startTime.format(Util.JAVA_FORMATTER),
			"production" to turn.production,
			"isGameOver" to turn.isGameOver,
			"isPaused" to turn.isPaused,
			"isStarted" to turn.isStarted,
			"tick" to turn.tick
		)
		if (showChildren) {
			output = output.plus("teams" to game.getTeams().map { it.toOutput(showParent = false) })
		}
		return output.toSortedMap()
	}

	private fun parseGame(ID: Long, response: Map<String, Any>): Game {
		return GameTable.insert(
			ID = ID,
			name = response["name"] as String,
			totalStars = (response["total_stars"] as Double).roundToInt(),
			victoryStars = (response["stars_for_victory"] as Double).roundToInt(),
			admin = (response["admin"] as Double).roundToInt(),
			fleetSpeed = response["fleet_speed"] as Double,
			isTurnBased = (response["turn_based"] as Double) == 1.0,
			productionRate = (response["production_rate"] as Double).roundToInt(),
			tickRate = (response["tick_rate"] as Double).roundToInt(),
			tradeCost = (response["trade_cost"] as Double).roundToInt(),
			turnBasedTimeout = (response["turn_based_time_out"] as Double).roundToInt()
		)
	}

	private fun parseTurn(game: Game, response: Map<String, Any>): GameTurn {
		return GameTurnTable.insert(
			game = game,
			startTime = LocalDateTime.ofInstant(
				Instant.ofEpochMilli((response["start_time"] as Double).roundToLong()),
				ZoneId.systemDefault()
			),
			production = (response["productions"] as Double).roundToInt(),
			isGameOver = (response["game_over"] as Double) == 1.0,
			isPaused = response["paused"] as Boolean,
			isStarted = response["started"] as Boolean,
			productionCounter = (response["production_counter"] as Double).roundToInt(),
			tick = (response["tick"] as Double).roundToInt(),
			tickFragment = (response["tick_fragment"] as Double).roundToInt(),
			tradeScanned = (response["trade_scanned"] as Double).roundToInt(),
			war = (response["war"] as Double).roundToInt()
		)
	}
}