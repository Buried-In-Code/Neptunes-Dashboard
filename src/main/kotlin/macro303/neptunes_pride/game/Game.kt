package macro303.neptunes_pride.game

import com.google.gson.annotations.SerializedName
import macro303.neptunes_pride.player.Player
import macro303.neptunes_pride.star.Star
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

internal data class Game(
	val admin: Int,
	val fleets: Any?,
	@SerializedName(value = "fleet_speed") val fleetSpeed: Double,
	@SerializedName(value = "started") val hasStarted: Boolean,
	@SerializedName(value = "game_over") private val gameOver: Int,
	@SerializedName(value = "paused") val isPaused: Boolean,
	val name: String,
	@SerializedName(value = "now") private val nowInstant: Long,
	@SerializedName(value = "production_rate") val payRate: Int,
	@SerializedName(value = "players") private val playerMap: HashMap<String, Player>,
	@SerializedName(value = "player_uid") val playerUID: Int,
	@SerializedName(value = "production_counter") val productionCounter: Int,
	@SerializedName(value = "start_time") private val startTimeInstant: Long,
	@SerializedName(value = "stars") private val starMap: HashMap<String, Star>,
	@SerializedName(value = "stars_for_victory") val starVictory: Int,
	val tick: Int,
	@SerializedName(value = "tick_fragment") val tickFragment: Int,
	@SerializedName(value = "tick_rate") val tickRate: Int,
	@SerializedName(value = "turn_based_time_out") val timeOut: Long,
	@SerializedName(value = "total_stars") val totalStars: Int,
	@SerializedName(value = "trade_cost") val tradeCost: Int,
	@SerializedName(value = "trade_scanned") val tradeScanned: Int,
	@SerializedName(value = "productions") val turn: Int,
	@SerializedName(value = "turn_based") val turnBased: Int,
	val war: Int
) {
	val isGameOver: Boolean
		get() = gameOver == 1

	val now: LocalDateTime
		get() = LocalDateTime.ofInstant(Instant.ofEpochMilli(nowInstant), ZoneId.systemDefault())

	val startTime: LocalDateTime
		get() = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTimeInstant), ZoneId.systemDefault())

	val stars: TreeSet<Star>
		get() = TreeSet(starMap.values)

	val players: TreeSet<Player>
		get() = TreeSet(playerMap.values)

	override fun toString(): String {
		return "Game(admin=$admin, fleets=$fleets, fleetSpeed=$fleetSpeed, hasStarted=$hasStarted, gameOver=$gameOver, isPaused=$isPaused, name='$name', nowInstant=$nowInstant, payRate=$payRate, playerMap=$playerMap, playerUID=$playerUID, productionCounter=$productionCounter, startTimeInstant=$startTimeInstant, starMap=$starMap, starVictory=$starVictory, tick=$tick, tickFragment=$tickFragment, tickRate=$tickRate, timeOut=$timeOut, totalStars=$totalStars, tradeCost=$tradeCost, tradeScanned=$tradeScanned, turn=$turn, turnBased=$turnBased, war=$war)"
	}
}