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
	private val game_over: Int,
	@SerializedName(value = "started") val hasStarted: Boolean,
	@SerializedName(value = "paused") val isPaused: Boolean,
	val name: String,
	private val now: Long,
	@SerializedName(value = "production_rate") val payRate: Int,
	private val players: HashMap<String, Player>,
	@SerializedName(value = "player_uid") val playerUID: Int,
	@SerializedName(value = "production_counter") val productionCounter: Int,
	private val stars: HashMap<String, Star>,
	private val start_time: Long,
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
		get() = game_over == 1

	val nowDateTime: LocalDateTime
		get() = LocalDateTime.ofInstant(Instant.ofEpochMilli(now), ZoneId.systemDefault())

	val playerSet: TreeSet<Player>
		get() = TreeSet(players.values)

	val starSet: TreeSet<Star>
		get() = TreeSet(stars.values)

	val startDateTime: LocalDateTime
		get() = LocalDateTime.ofInstant(Instant.ofEpochMilli(start_time), ZoneId.systemDefault())

	override fun toString(): String {
		return "Game(admin=$admin, fleets=$fleets, fleetSpeed=$fleetSpeed, game_over=$game_over, hasStarted=$hasStarted, isPaused=$isPaused, name='$name', now=$now, payRate=$payRate, players=$players, playerUID=$playerUID, productionCounter=$productionCounter, stars=$stars, start_time=$start_time, starVictory=$starVictory, tick=$tick, tickFragment=$tickFragment, tickRate=$tickRate, timeOut=$timeOut, totalStars=$totalStars, tradeCost=$tradeCost, tradeScanned=$tradeScanned, turn=$turn, turnBased=$turnBased, war=$war)"
	}
}