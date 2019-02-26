package macro.neptunes.backend

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

/**
 * Created by Macro303 on 2019-Feb-26.
 */
class UpdateDeserializer : JsonDeserializer<PlayerUpdate?> {

	override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): PlayerUpdate? {
		val jsonObject: JsonObject = json!!.asJsonObject

		val alias = jsonObject["alias"].asString
		if (alias.isNullOrBlank()) return null
		val economy = jsonObject["total_economy"].asInt
		val industry = jsonObject["total_industry"].asInt
		val science = jsonObject["total_science"].asInt
		val stars = jsonObject["total_stars"].asInt
		val fleet = jsonObject["total_fleets"].asInt
		val ships = jsonObject["total_strength"].asInt
		val isActive = jsonObject["conceded"].asInt == 0

		return PlayerUpdate(
			alias = alias,
			economy = economy,
			industry = industry,
			science = science,
			stars = stars,
			fleet = fleet,
			ships = ships,
			isActive = isActive
		)
	}
}