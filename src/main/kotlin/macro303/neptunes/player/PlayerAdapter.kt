package macro303.neptunes.player

import com.google.gson.*
import macro303.neptunes.Util
import macro303.neptunes.technology.Technology
import java.lang.reflect.Type
import java.util.*

/**
 * Created by Macro303 on 2018-05-10.
 */
class PlayerAdapter : JsonSerializer<Player>, JsonDeserializer<Player> {

	@Throws(JsonParseException::class)
	override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Player {
		val obj = json.asJsonObject

		val AI = obj.get("ai").asInt != 0
		val alias = obj.get("alias").asString
		val conceded = obj.get("conceded").asInt != 0
		val name = Util.config.playerNames.getOrDefault(alias, "Unknown")
		val team = Util.config.teams.entries.stream().filter({ entry -> entry.value.contains(name) }).findFirst()
			.map { it.key }.orElse("Unknown")
		val techMap = obj.get("tech").asJsonObject
		val technologies = HashMap<String, Technology>()
		for ((key, value) in techMap.entrySet()) {
			technologies[key] = context.deserialize(value, Technology::class.java)
		}
		val totalEconomy = obj.get("total_economy").asInt
		val totalIndustry = obj.get("total_industry").asInt
		val totalScience = obj.get("total_science").asInt
		val totalShips = obj.get("total_strength").asInt
		val totalStars = obj.get("total_stars").asInt

		return Player(
			AI,
			alias,
			conceded,
			name,
			team,
			technologies,
			totalEconomy,
			totalIndustry,
			totalScience,
			totalShips,
			totalStars
		)
	}

	override fun serialize(src: Player, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
		val obj = JsonObject()

		obj.addProperty("AI", src.ai)
		obj.addProperty("alias", src.alias)
		obj.addProperty("conceded", src.conceded)
		obj.addProperty("name", src.name)
		obj.addProperty("team", src.team)
		val technologies = context.serialize(src.technologies)
		obj.add("technologies", technologies)
		obj.addProperty("totalEconomy", src.totalEconomy)
		obj.addProperty("totalIndustry", src.totalIndustry)
		obj.addProperty("totalScience", src.totalScience)
		obj.addProperty("totalShips", src.totalShips)
		obj.addProperty("totalStars", src.totalStars)

		return obj
	}
}