package macro.dashboard.neptunes.backend

import com.google.gson.annotations.SerializedName
import macro.dashboard.neptunes.Config.Companion.CONFIG
import macro.dashboard.neptunes.InternalServerErrorResponse
import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.cycle.CycleTable
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.PlayerTable
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2019-Feb-26.
 */
object Proteus {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	@Suppress("UNCHECKED_CAST")
	fun getGame(gameID: Long, code: String) {
		val response = RESTClient.postRequest(url = "https://np.ironhelmet.com/api", gameID = gameID, code = code)
		if (response["Code"] == 200) {
			if (!response["Response"].toString().contains("fleet_price"))
				throw InternalServerErrorResponse(message = "Invalid response from backend")
			val game = Util.GSON.fromJson<ProteusGame>(response["Response"].toString(), ProteusGame::class.java)
			val valid = GameTable.insert(ID = gameID, update = game)
			if (!valid)
				GameTable.update(update = game)
			game.players.values.filter { it.alias.isNotBlank() }.forEach {
				PlayerTable.insert(gameID = gameID, update = it)
				PlayerTable.select(alias = it.alias)?.apply {
					CycleTable.insert(playerID = this.ID, cycle = game.tick / CONFIG.gameCycle, update = it)
				} ?: throw InternalServerErrorResponse(message = "Unable to Find Player => ${it.alias}")
			}
		}
	}
}

data class ProteusGame(
	//fleets
	@SerializedName(value = "fleet_speed")
	val fleetSpeed: Double,
	@SerializedName(value = "paused")
	val isPaused: Boolean,
	val productions: Int,
	@SerializedName(value = "fleet_price")
	val fleetPrice: Int,
	@SerializedName(value = "tick_fragment")
	val tickFragment: Int,
	val now: Long,
	@SerializedName(value = "tick_rate")
	val tickRate: Int,
	@SerializedName(value = "production_rate")
	val productionRate: Int,
	//stars
	@SerializedName(value = "stars_for_victory")
	val victoryStars: Int,
	@SerializedName(value = "game_over")
	val gameOver: Int,
	@SerializedName(value = "started")
	val isStarted: Boolean,
	@SerializedName(value = "start_time")
	val startTime: Long,
	@SerializedName(value = "total_stars")
	val totalStars: Int,
	@SerializedName(value = "production_counter")
	val productionCounter: Int,
	@SerializedName(value = "trade_scanned")
	val tradeScanned: Int,
	val tick: Int,
	@SerializedName(value = "trade_cost")
	val tradeCost: Int,
	val name: String,
	@SerializedName(value = "player_uid")
	val playerUid: Int,
	val admin: Int,
	@SerializedName(value = "turn_based")
	val turnBased: Int,
	val war: Int,
	val players: Map<String, ProteusPlayer>,
	@SerializedName(value = "turn_based_time_out")
	val cycleTimeout: Long
)

data class ProteusPlayer(
	val ai: Int,
	@SerializedName(value = "total_industry")
	val industry: Int,
	val regard: Int,
	@SerializedName(value = "total_science")
	val science: Int,
	val uid: Int,
	@SerializedName(value = "color")
	val colour: Int,
	val huid: Int,
	@SerializedName(value = "total_stars")
	val stars: Int,
	@SerializedName(value = "total_fleets")
	val fleet: Int,
	@SerializedName(value = "total_strength")
	val ships: Int,
	val alias: String,
	val shape: Int,
	val race: List<Int>,
	val tech: Map<String, ProteusTech>,
	val avatar: Int,
	val conceded: Int,
	val ready: Int,
	@SerializedName(value = "total_economy")
	val economy: Int,
	@SerializedName(value = "missed_turns")
	val missed: Int,
	@SerializedName(value = "karma_to_give")
	val karma: Int
)

data class ProteusTech(
	val value: Double,
	val level: Int
)