package macro.dashboard.neptunes.backend

import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import macro.dashboard.neptunes.GeneralException
import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.player.TurnTable
import macro.dashboard.neptunes.technology.TechnologyTable
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2019-Feb-26.
 */
object Neptunes {
	private val LOGGER = LogManager.getLogger(Neptunes::class.java)
	private const val BASE_URL = "http://nptriton.cqproject.net/game"

	fun getGame(gameID: Long, code: String) {
		val response = RESTClient.postRequest(url = "https://np.ironhelmet.com/api", gameID = gameID, code = code)
		if (response["Code"] == 200) {
			val game = Util.GSON.fromJson<GameUpdate>(response["Response"].toString(), GameUpdate::class.java)
			val valid = GameTable.insert(ID = gameID, code = code, update = game)
			if(!valid)
				GameTable.update(ID = gameID, update = game)
			game.players.values.forEach { update ->
				PlayerTable.insert(gameID = gameID, update = update)
				val player = PlayerTable.search(gameID = gameID, alias = update.alias).firstOrNull()
					?: throw GeneralException()
				TurnTable.insert(playerID = player.ID, tick = game.tick, update = update)
				val turn = TurnTable.select(playerID = player.ID, tick = game.tick) ?: throw GeneralException()
				update.tech.forEach { name, tech ->
					TechnologyTable.insert(turnID = turn.ID, name = name, update = tech)
				}
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
	//fleets
	@SerializedName(value = "fleet_speed")
	val fleetSpeed: Double,
	@SerializedName(value = "paused")
	val isPaused: Boolean,
	@SerializedName(value = "productions")
	val production: Int,
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
	val players: Map<String, PlayerUpdate>,
	@SerializedName(value = "turn_based_time_out")
	val turnBasedTimeout: Long
)

data class PlayerUpdate(
	@SerializedName(value = "total_industry")
	val industry: Int,
	val regard: Int,
	@SerializedName(value = "total_science")
	val science: Int,
	val uid: Int,
	val ai: Int,
	val huid: Int,
	@SerializedName(value = "total_stars")
	val stars: Int,
	@SerializedName(value = "total_fleets")
	val fleet: Int,
	@SerializedName(value = "total_strength")
	val ships: Int,
	val alias: String,
	val tech: Map<String, TechUpdate>,
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

data class TechUpdate(
	val value: Double,
	val level: Int
)