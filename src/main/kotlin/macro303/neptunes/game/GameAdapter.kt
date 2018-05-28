package macro303.neptunes.game

import com.google.gson.*
import java.lang.reflect.Type
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Created by Macro303 on 2018-05-10.
 */
class GameAdapter : JsonSerializer<Game>, JsonDeserializer<Game> {

	@Throws(JsonParseException::class)
	override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Game {
		val obj = json.asJsonObject

		val gameOver = obj.get("game_over").asInt == 1
		val name = obj.get("name").asString
		val paused = obj.get("paused").asBoolean
		val started = obj.get("started").asBoolean
		val startTime =
			LocalDateTime.ofInstant(Instant.ofEpochMilli(obj.get("start_time").asLong), ZoneId.systemDefault())
		val victory = obj.get("stars_for_victory").asInt
		val totalStars = obj.get("total_stars").asInt
		val turnTime =
			LocalDateTime.ofInstant(Instant.ofEpochMilli(obj.get("turn_based_time_out").asLong), ZoneId.systemDefault())

		return Game(gameOver, name, paused, started, startTime, totalStars, turnTime, victory)
	}

	override fun serialize(src: Game, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
		val obj = JsonObject()

		obj.addProperty("gameOver", src.gameOver)
		obj.addProperty("name", src.name)
		obj.addProperty("paused", src.paused)
		obj.addProperty("started", src.started)
		obj.addProperty("startTime", src.startTime.format(formatter))
		obj.addProperty("victory", src.victory)
		obj.addProperty("totalStars", src.totalStars)

		return obj
	}

	companion object {
		private val formatter = DateTimeFormatter.ofPattern("MMM-dd HH:mm")
	}
}