package macro303.neptunes_pride

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap
import java.time.ZoneId
import java.time.Instant

internal class Game {
	var fleets: Any? = null
		private set
	@SerializedName(value = "fleet_speed")
	var fleetSpeed: Double = 0.0
		private set
	@SerializedName(value = "paused")
	var isPaused: Boolean = true
		private set
	@SerializedName(value = "productions")
	var turn: Int = 0
		private set
	@SerializedName(value = "tick_fragment")
	var tickFragment: Int = 0
		private set
	var now: Long = 0
		private set
	@SerializedName(value = "tick_rate")
	var tickRate: Int = 0
		private set
	@SerializedName(value = "production_rate")
	var payday: Int = 0
		private set
	private val stars: HashMap<String, Star> = HashMap()
	@SerializedName(value = "stars_for_victory")
	var starVictory: Int = 0
		private set
	@SerializedName(value = "game_over")
	var gameOver: Int = 0
		private set
	@SerializedName(value = "started")
	var isStarted: Boolean = false
		private set
	@SerializedName(value = "start_time")
	private var startTime: Long = 0
	@SerializedName(value = "total_stars")
	var totalStars: Int = 0
		private set
	@SerializedName(value = "production_counter")
	var productionCounter: Int = 0
		private set
	@SerializedName(value = "trade_scanned")
	var tradeScanned: Int = 0
		private set
	var tick: Int = 0
		private set
	@SerializedName(value = "trade_cost")
	var tradeCost: Int = 0
		private set
	var name: String = ""
		private set
	@SerializedName(value = "player_uid")
	var playerUID: Int = 0
		private set
	var admin: Int = 0
		private set
	@SerializedName(value = "turn_based")
	var turnBased: Int = 0
		private set
	var war: Int = 0
		private set
	private val players = HashMap<String, Player>()
	@SerializedName(value = "turn_based_time_out")
	var timeOut: Long = 0
		private set

	fun getStartTime(): LocalDateTime{
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault())
	}

	fun getStars(): TreeSet<Star> {
		return TreeSet(stars.values)
	}

	fun getPlayers(): TreeSet<Player> {
		return TreeSet(players.values)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Game) return false

		if (fleets != other.fleets) return false
		if (fleetSpeed != other.fleetSpeed) return false
		if (isPaused != other.isPaused) return false
		if (turn != other.turn) return false
		if (tickFragment != other.tickFragment) return false
		if (now != other.now) return false
		if (tickRate != other.tickRate) return false
		if (payday != other.payday) return false
		if (stars != other.stars) return false
		if (starVictory != other.starVictory) return false
		if (gameOver != other.gameOver) return false
		if (isStarted != other.isStarted) return false
		if (startTime != other.startTime) return false
		if (totalStars != other.totalStars) return false
		if (productionCounter != other.productionCounter) return false
		if (tradeScanned != other.tradeScanned) return false
		if (tick != other.tick) return false
		if (tradeCost != other.tradeCost) return false
		if (name != other.name) return false
		if (playerUID != other.playerUID) return false
		if (admin != other.admin) return false
		if (turnBased != other.turnBased) return false
		if (war != other.war) return false
		if (players != other.players) return false
		if (timeOut != other.timeOut) return false

		return true
	}

	override fun hashCode(): Int {
		var result = fleets?.hashCode() ?: 0
		result = 31 * result + fleetSpeed.hashCode()
		result = 31 * result + isPaused.hashCode()
		result = 31 * result + turn
		result = 31 * result + tickFragment
		result = 31 * result + now.hashCode()
		result = 31 * result + tickRate
		result = 31 * result + payday
		result = 31 * result + stars.hashCode()
		result = 31 * result + starVictory
		result = 31 * result + gameOver
		result = 31 * result + isStarted.hashCode()
		result = 31 * result + startTime.hashCode()
		result = 31 * result + totalStars
		result = 31 * result + productionCounter
		result = 31 * result + tradeScanned
		result = 31 * result + tick
		result = 31 * result + tradeCost
		result = 31 * result + name.hashCode()
		result = 31 * result + playerUID
		result = 31 * result + admin
		result = 31 * result + turnBased
		result = 31 * result + war
		result = 31 * result + players.hashCode()
		result = 31 * result + timeOut.hashCode()
		return result
	}

	override fun toString(): String {
		return "Game(fleets=$fleets, fleetSpeed=$fleetSpeed, isPaused=$isPaused, turn=$turn, tickFragment=$tickFragment, now=$now, tickRate=$tickRate, payday=$payday, stars=$stars, starVictory=$starVictory, gameOver=$gameOver, isStarted=$isStarted, startTime=$startTime, totalStars=$totalStars, productionCounter=$productionCounter, tradeScanned=$tradeScanned, tick=$tick, tradeCost=$tradeCost, name='$name', playerUID=$playerUID, admin=$admin, turnBased=$turnBased, war=$war, players=$players, timeOut=$timeOut)"
	}
}