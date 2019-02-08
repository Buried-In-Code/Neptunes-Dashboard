package macro.neptunes.core.game

import macro.neptunes.core.Config.Companion.CONFIG
import macro.neptunes.data.RESTClient
import org.apache.logging.log4j.LogManager
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * Created by Macro303 on 2018-Nov-15.
 */
object GameHandler {
	private val LOGGER = LogManager.getLogger(GameHandler::class.java)
	lateinit var game: Game

	private fun parse(data: Map<String, Any?>): Game? {
		val fleetSpeed = data.getOrDefault("fleet_speed", null)?.toString()?.toDoubleOrNull()
			?: return null
		val isPaused = data.getOrDefault("paused", null)?.toString()?.toBoolean()
			?: return null
		val productions = data.getOrDefault("productions", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val tickFragment = data.getOrDefault("tick_fragment", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val tickRate = data.getOrDefault("tick_rate", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val productionRate = data.getOrDefault("production_rate", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val victoryStars = data.getOrDefault("stars_for_victory", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val isGameOver = data.getOrDefault("game_over", null)?.toString()?.toBoolean()
			?: return null
		val isStarted = data.getOrDefault("started", null)?.toString()?.toBoolean()
			?: return null
		val startTimeLong = data.getOrDefault("start_time", null)?.toString()?.toDoubleOrNull()?.roundToLong()
			?: return null
		val startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTimeLong), ZoneId.systemDefault())
		val totalStars = data.getOrDefault("total_stars", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val productionCounter = data.getOrDefault("production_counter", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val tradeScanned = data.getOrDefault("trade_scanned", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val tick = data.getOrDefault("tick", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val tradeCost = data.getOrDefault("trade_cost", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val name = data.getOrDefault("name", null)?.toString()
			?: return null
		val admin = data.getOrDefault("admin", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val isTurnBased = data.getOrDefault("turn_based", null)?.toString()?.toBoolean()
			?: return null
		val war = data.getOrDefault("war", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val turnBasedTimeout = data.getOrDefault("turn_based_time_out", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		return Game(
			fleetSpeed = fleetSpeed,
			isPaused = isPaused,
			productions = productions,
			tickFragment = tickFragment,
			tickRate = tickRate,
			productionRate = productionRate,
			victoryStars = victoryStars,
			isGameOver = isGameOver,
			isStarted = isStarted,
			startTime = startTime,
			totalStars = totalStars,
			productionCounter = productionCounter,
			tradeScanned = tradeScanned,
			tick = tick,
			tradeCost = tradeCost,
			name = name,
			admin = admin,
			isTurnBased = isTurnBased,
			war = war,
			turnBasedTimeout = turnBasedTimeout
		)
	}

	@Suppress("UNCHECKED_CAST")
	fun refreshData(): Boolean {
		LOGGER.info("Refreshing Game Data")
		val response = RESTClient.getRequest(endpoint = "/basic")
		val game = parse(data = response["Data"] as Map<String, Any?>)
		if (game == null) {
			LOGGER.error("Unable to find game with Game ID: ${CONFIG.gameID}")
			return false
		}
		this.game = game
		return true
	}
}