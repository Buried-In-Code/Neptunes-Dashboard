package macro.dashboard.neptunes.backend

import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import macro.dashboard.neptunes.GeneralException
import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.player.PlayerTurnTable
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2019-Feb-26.
 */
object Neptunes {
	private val LOGGER = LogManager.getLogger(Neptunes::class.java)
	private const val BASE_URL = "http://nptriton.cqproject.net/game"

	fun getGame(gameID: Long) {
		val response = RESTClient(baseUrl = BASE_URL, gameID = gameID).getRequest(endpoint = "/basic")
		if (response["Code"] == 200) {
			val game = Util.GSON.fromJson<GameUpdate>(response["Data"].toString(), GameUpdate::class.java)
			val current = GameTable.select(ID = gameID)
			if (current == null)
				GameTable.insert(ID = gameID, update = game)
			else
				GameTable.update(ID = gameID, update = game)
			getPlayers(gameID = gameID, tick = game.tick)
		}
	}

	fun getPlayers(gameID: Long, tick: Int) {
		val response = RESTClient(baseUrl = BASE_URL, gameID = gameID).getRequest(endpoint = "/players")
		if (response["Code"] == 200) {
			val players = response["Data"].toString().JsonToPlayerMap()
			players.values.filterNotNull().forEach { update ->
				PlayerTable.insert(gameID = gameID, update = update)
				val player = PlayerTable.search(gameID = gameID, alias = update.alias).firstOrNull()
					?: throw GeneralException()
				PlayerTurnTable.insert(playerID = player.ID, tick = tick, update = update)
			}
		}
	}

	@Throws(JsonSyntaxException::class)
	private fun String.JsonToPlayerMap(): Map<String, PlayerUpdate?> {
		if (this.isBlank()) return emptyMap()
		val type = object : TypeToken<Map<String, PlayerUpdate?>>() {
		}.type
		return Util.GSON.fromJson(this, type) ?: emptyMap()
	}
}

data class GameUpdate(
	@SerializedName(value = "name")
	val name: String,
	@SerializedName(value = "total_stars")
	val totalStars: Int,
	@SerializedName(value = "stars_for_victory")
	val victoryStars: Int,
	@SerializedName(value = "admin")
	val admin: Int,
	@SerializedName(value = "fleet_speed")
	val fleetSpeed: Double,
	@SerializedName(value = "turn_based")
	val turnBased: Int,
	@SerializedName(value = "production_rate")
	val productionRate: Int,
	@SerializedName(value = "tick_rate")
	val tickRate: Int,
	@SerializedName(value = "start_time")
	val startTime: Long,
	@SerializedName(value = "game_over")
	val gameOver: Int,
	@SerializedName(value = "paused")
	val isPaused: Boolean,
	@SerializedName(value = "started")
	val isStarted: Boolean,
	@SerializedName(value = "trade_cost")
	val tradeCost: Int,
	@SerializedName(value = "productions")
	val production: Int,
	@SerializedName(value = "production_counter")
	val productionCounter: Int,
	@SerializedName(value = "tick")
	val tick: Int,
	@SerializedName(value = "tick_fragment")
	val tickFragment: Int,
	@SerializedName(value = "trade_scanned")
	val tradeScanned: Int,
	@SerializedName(value = "war")
	val war: Int,
	@SerializedName(value = "turn_based_time_out")
	val turnBasedTimeout: Long
)

data class PlayerUpdate(
	@SerializedName(value = "alias")
	val alias: String,
	@SerializedName(value = "total_economy")
	val economy: Int,
	@SerializedName(value = "total_industry")
	val industry: Int,
	@SerializedName(value = "total_science")
	val science: Int,
	@SerializedName(value = "total_stars")
	val stars: Int,
	@SerializedName(value = "total_fleets")
	val fleet: Int,
	@SerializedName(value = "total_strength")
	val ships: Int,
	@SerializedName(value = "conceded")
	val isActive: Boolean
)