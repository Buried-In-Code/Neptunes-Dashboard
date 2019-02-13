package macro.neptunes.game

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import macro.neptunes.config.Config.Companion.CONFIG
import java.lang.reflect.Type
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by Macro303 on 2019-Feb-12.
 */
object GameDeserializer : JsonDeserializer<Game> {

	override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Game {
		val jsonObject: JsonObject = json!!.asJsonObject

		val name = jsonObject["name"].asString
		val startTimeLong = jsonObject["start_time"].asLong
		val startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTimeLong), ZoneId.systemDefault())
		val totalStars = jsonObject["total_stars"].asInt
		val victoryStars = jsonObject["stars_for_victory"].asInt
		val productions = jsonObject["productions"].asInt
		val admin = jsonObject["admin"].asInt
		val fleetSpeed = jsonObject["fleet_speed"].asDouble
		val isGameOver = jsonObject["game_over"].asBoolean
		val isPaused = jsonObject["paused"].asBoolean
		val isStarted = jsonObject["started"].asBoolean
		val isTurnBased = jsonObject["turn_based"].asBoolean
		val productionCounter = jsonObject["production_counter"].asInt
		val productionRate = jsonObject["production_rate"].asInt
		val tick = jsonObject["tick"].asInt
		val tickFragment = jsonObject["tick_fragment"].asInt
		val tickRate = jsonObject["tick_rate"].asInt
		val tradeCost = jsonObject["trade_cost"].asInt
		val tradeScanned = jsonObject["trade_scanned"].asInt
		val turnBasedTimeout = jsonObject["turn_based_time_out"].asInt
		val war = jsonObject["war"].asInt

		return Game(
			ID = CONFIG.gameID,
			name = name,
			startTime = startTime,
			totalStars = totalStars,
			victoryStars = victoryStars,
			productions = productions,
			lastUpdated = LocalDateTime.now(),
			admin = admin,
			fleetSpeed = fleetSpeed,
			isGameOver = isGameOver,
			isPaused = isPaused,
			isStarted = isStarted,
			isTurnBased = isTurnBased,
			productionCounter = productionCounter,
			productionRate = productionRate,
			tick = tick,
			tickFragment = tickFragment,
			tickRate = tickRate,
			tradeCost = tradeCost,
			tradeScanned = tradeScanned,
			turnBasedTimeout = turnBasedTimeout,
			war = war
		)
	}
}