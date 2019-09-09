package macro.dashboard.neptunes

import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import macro.dashboard.neptunes.config.Config.Companion.CONFIG
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
		val response = Util.postRequest(url = "https://np.ironhelmet.com/api", gameID = gameID, code = code)
		response ?: return
		LOGGER.debug("Response: $response")
		try {
			val gameObject = Util.GSON.fromJson<ProteusData>(response, ProteusData::class.java).scanningData
			LOGGER.debug("Game: $gameObject")
			val valid = GameTable.insert(ID = gameID, update = gameObject)
			if (!valid)
				GameTable.update(update = gameObject)
			gameObject.players.values.filter { it.alias.isNotBlank() }.forEach {
				PlayerTable.insert(gameID = gameID, update = it)
				PlayerTable.search(alias = it.alias).firstOrNull()?.apply {
					CycleTable.insert(playerID = this.ID, cycle = gameObject.tick / CONFIG.game.cycle, update = it)
				} ?: throw InternalServerErrorResponse(message = "Unable to Find Player => ${it.alias}")
			}
		}catch (jse: JsonSyntaxException){
			throw InternalServerErrorResponse(message = "Invalid response from Backend")
		}
	}
}

internal class ProteusData {
	@SerializedName(value = "scanning_data")
	var scanningData: ProteusGame = ProteusGame()
}

internal class ProteusGame {
	//fleets
	@SerializedName(value = "fleet_speed")
	var fleetSpeed: Double = 0.0
	@SerializedName(value = "paused")
	var isPaused: Boolean = false
	var productions: Int = 0
	@SerializedName(value = "fleet_price")
	var fleetPrice: Int = 0
	@SerializedName(value = "tick_fragment")
	var tickFragment: Int = 0
	var now: Long = 0
	@SerializedName(value = "tick_rate")
	var tickRate: Int = 0
	@SerializedName(value = "production_rate")
	var productionRate: Int = 0
	//stars
	@SerializedName(value = "stars_for_victory")
	var victoryStars: Int = 0
	@SerializedName(value = "game_over")
	var gameOver: Int = 0
	@SerializedName(value = "started")
	var isStarted: Boolean = false
	@SerializedName(value = "start_time")
	var startTime: Long = 0
	@SerializedName(value = "total_stars")
	var totalStars: Int = 0
	@SerializedName(value = "production_counter")
	var productionCounter: Int = 0
	@SerializedName(value = "trade_scanned")
	var tradeScanned: Int = 0
	var tick: Int = 0
	@SerializedName(value = "trade_cost")
	var tradeCost: Int = 0
	var name: String = ""
	@SerializedName(value = "player_uid")
	var playerUid: Int = 0
	var admin: Int = 0
	@SerializedName(value = "turn_based")
	var turnBased: Int = 0
	var war: Int = 0
	var players: Map<String, ProteusPlayer> = emptyMap()
	@SerializedName(value = "turn_based_time_out")
	var cycleTimeout: Long = 0
}

internal class ProteusPlayer {
	var ai: Int = 0
	@SerializedName(value = "total_industry")
	var industry: Int = 0
	var regard: Int = 0
	@SerializedName(value = "total_science")
	var science: Int = 0
	var uid: Int = 0
	@SerializedName(value = "color")
	var colour: Int = 0
	var huid: Int = 0
	@SerializedName(value = "total_stars")
	var stars: Int = 0
	@SerializedName(value = "total_fleets")
	var fleet: Int = 0
	@SerializedName(value = "total_strength")
	var ships: Int = 0
	var alias: String = ""
	var shape: Int = 0
	var race: List<Int> = emptyList()
	var tech: Map<String, ProteusTech> = emptyMap()
	var avatar: Int = 0
	var conceded: Int = 0
	var ready: Int = 0
	@SerializedName(value = "total_economy")
	var economy: Int = 0
	@SerializedName(value = "missed_turns")
	var missed: Int = 0
	@SerializedName(value = "karma_to_give")
	var karma: Int = 0
}

internal class ProteusTech {
	var value: Double = 0.0
	var level: Int = 0
}