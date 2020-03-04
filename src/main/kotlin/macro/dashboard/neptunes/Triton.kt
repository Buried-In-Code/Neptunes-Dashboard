package macro.dashboard.neptunes

import kong.unirest.json.JSONObject
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.player.Player
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.tick.Tick
import macro.dashboard.neptunes.tick.TickTable
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by Macro303 on 2019-Nov-25
 */
object Triton {
	private val LOGGER = LogManager.getLogger(Triton::class.java)
	private val URL = "https://np.ironhelmet.com/api"

	@Suppress("UNCHECKED_CAST")
	fun getGame(gameId: Long, code: String) {
		val request = Util.postRequest(url = URL, gameId = gameId, code = code) ?: return
		val response = request.`object`.getJSONObject("scanning_data")
		val game = parseGame(gameId, code, response)
		parsePlayers(game, response.getJSONObject("players"))
	}

	private fun parseGame(gameId: Long, gameCode: String, json: JSONObject): Game = transaction(db = Util.database) {
		val game = Game.findById(gameId) ?: Game.new(gameId) {
			code = gameCode
			fleetSpeed = json.getDouble("fleet_speed")
			tickRate = json.getInt("tick_rate")
			productionRate = json.getInt("production_rate")
			victoryStars = json.getInt("stars_for_victory")
			totalStars = json.getInt("total_stars")
			isTradeScanned = json.getInt("trade_scanned") == 0
			tradeCost = json.getInt("trade_cost")
			name = json.getString("name")
			isTurnBased = json.getInt("turn_based") != 0
			gameType = "Triton"
		}
		game.isPaused = json.getBoolean("paused")
		game.productions = json.getInt("productions")
		game.tickFragment = json.getInt("tick_fragment")
		game.isGameOver = json.getInt("game_over") != 0
		game.isStarted = json.getBoolean("started")
		game.startTime = LocalDateTime.ofInstant(
			Instant.ofEpochMilli(json.getLong("start_time")),
			ZoneId.of("Pacific/Auckland")
		)
		game.productionCounter = json.getInt("production_counter")
		game.tick = json.getInt("tick")
		game.war = json.getInt("war")
		game.turnTimeout = LocalDateTime.ofInstant(
			Instant.ofEpochMilli(json.getLong("turn_based_time_out")),
			ZoneId.of("Pacific/Auckland")
		)

		return@transaction game
	}

	private fun parsePlayers(game: Game, json: JSONObject) {
		json.keySet().forEach {
			transaction(db = Util.database) {
				val playerObj = json.getJSONObject(it)
				val alias = playerObj.getString("alias")
				if (alias.isNullOrEmpty())
					return@transaction
				val player = Player.find {
					PlayerTable.gameCol eq game.id and (PlayerTable.aliasCol eq alias)
				}.limit(1).firstOrNull() ?: Player.new {
					this.game = game
					this.alias = alias
				}

				val cycle = Tick.find {
					TickTable.gameCol eq game.id and (TickTable.playerCol eq player.id) and (TickTable.tickCol eq game.tick)
				}.limit(1).firstOrNull() ?: Tick.new {
					this.game = game
					this.player = player
					tick = game.tick
				}
				cycle.industry = playerObj.getInt("total_industry")
				cycle.science = playerObj.getInt("total_science")
				cycle.stars = playerObj.getInt("total_stars")
				cycle.fleets = playerObj.getInt("total_fleets")
				cycle.ships = playerObj.getInt("total_strength")
				cycle.isActive = playerObj.getInt("conceded") == 0
				cycle.economy = playerObj.getInt("total_economy")
				val techObj = playerObj.getJSONObject("tech")
				cycle.scanning = techObj.getJSONObject("scanning").getInt("level")
				cycle.propulsion = techObj.getJSONObject("propulsion").getInt("level")
				cycle.terraforming = techObj.getJSONObject("terraforming").getInt("level")
				cycle.research = techObj.getJSONObject("research").getInt("level")
				cycle.weapons = techObj.getJSONObject("weapons").getInt("level")
				cycle.banking = techObj.getJSONObject("banking").getInt("level")
				cycle.manufacturing = techObj.getJSONObject("manufacturing").getInt("level")
			}
		}
	}
}