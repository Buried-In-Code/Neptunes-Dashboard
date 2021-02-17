package macro.dashboard.v2.external

import kong.unirest.json.JSONObject
import macro.dashboard.Utils
import macro.dashboard.v2.schemas.*
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
	private const val BASE_URL = "https://np.ironhelmet.com/api"

	@Suppress("UNCHECKED_CAST")
	fun getGame(gameNumber: Long, apiCode: String, tickRate: Int) {
		val request = Utils.postRequest(url = BASE_URL, gameNumber = gameNumber, apiCode = apiCode) ?: return
		val response = request.`object`.getJSONObject("scanning_data")
		val game = parseGame(gameNumber, apiCode, tickRate, response)
		parsePlayers(game, response.getJSONObject("players"))
	}

	private fun parseGame(gameNumber: Long, apiCode: String, tickRate: Int, json: JSONObject): Game =
		transaction(db = Utils.DATABASE) {
			val game = Game.findById(gameNumber) ?: Game.new(gameNumber) {
				// Static Columns
				this.apiCode = apiCode
				this.tickRate = tickRate
				this.title = json.getString("name")
				this.type = Type.TRITON
				this.victoryStars = json.getInt("stars_for_victory")
				this.totalStars = json.getInt("total_stars")
				this.tradeCost = json.getInt("trade_cost")
				this.isTradeScanOnly = json.getInt("trade_scanned") == 0
				this.isTurnBased = json.getInt("turn_based") != 0
			}
			// Dynamic Fields
			game.isPaused = json.getBoolean("paused")
			game.isGameOver = json.getInt("game_over") != 0
			game.isStarted = json.getBoolean("started")
			game.startTime = LocalDateTime.ofInstant(
				Instant.ofEpochMilli(json.getLong("start_time")),
				ZoneId.of("Pacific/Auckland")
			)
			game.turn = (json.getInt("tick") / tickRate).toLong()
			game.nextTurn = LocalDateTime.ofInstant(
				Instant.ofEpochMilli(json.getLong("turn_based_time_out")),
				ZoneId.of("Pacific/Auckland")
			)

			return@transaction game
		}

	private fun parsePlayers(game: Game, json: JSONObject) {
		json.keySet().forEach {
			transaction(db = Utils.DATABASE) {
				val playerObj = json.getJSONObject(it)
				val username = playerObj.getString("alias")
				if (username.isNullOrEmpty())
					return@transaction
				val player = Player.find {
					PlayerTable.gameCol eq game.id and
							(PlayerTable.usernameCol eq username)
				}.limit(1).firstOrNull() ?: Player.new {
					this.game = game
					this.username = username
				}

				val turn = Turn.find {
					TurnTable.playerCol eq player.id and
							(TurnTable.turnCol eq game.turn)
				}.limit(1).firstOrNull() ?: Turn.new {
					this.player = player
					this.turn = game.turn
				}
				turn.industry = playerObj.getInt("total_industry")
				turn.science = playerObj.getInt("total_science")
				turn.stars = playerObj.getInt("total_stars")
				turn.carriers = playerObj.getInt("total_fleets")
				turn.ships = playerObj.getInt("total_strength")
				turn.isActive = playerObj.getInt("conceded") == 0
				turn.economy = playerObj.getInt("total_economy")
				val techObj = playerObj.getJSONObject("tech")
				turn.scanning = techObj.getJSONObject("scanning").getInt("level")
				turn.hyperspaceRange = techObj.getJSONObject("propulsion").getInt("level")
				turn.terraforming = techObj.getJSONObject("terraforming").getInt("level")
				turn.experimentation = techObj.getJSONObject("research").getInt("level")
				turn.weapons = techObj.getJSONObject("weapons").getInt("level")
				turn.banking = techObj.getJSONObject("banking").getInt("level")
				turn.manufacturing = techObj.getJSONObject("manufacturing").getInt("level")
			}
		}
	}
}