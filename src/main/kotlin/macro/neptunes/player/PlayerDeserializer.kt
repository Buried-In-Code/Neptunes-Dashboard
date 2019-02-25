package macro.neptunes.player

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import macro.neptunes.team.TeamTable
import macro.neptunes.technology.PlayerTechnology
import macro.neptunes.technology.Technology
import java.lang.reflect.Type

/**
 * Created by Macro303 on 2019-Feb-12.
 */
object PlayerDeserializer : JsonDeserializer<Player> {

	override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Player {
		val jsonObject: JsonObject = json!!.asJsonObject

		val teamName = "Free For All"
		TeamTable.select(name = teamName) ?: TeamTable.insert(name = teamName)
		val alias = jsonObject["alias"].asString
		val economy = jsonObject["total_economy"].asInt
		val industry = jsonObject["total_industry"].asInt
		val science = jsonObject["total_science"].asInt
		val stars = jsonObject["total_stars"].asInt
		val fleet = jsonObject["total_fleets"].asInt
		val ships = jsonObject["total_strength"].asInt
		val isActive = jsonObject["conceded"].asInt == 0

		//Technology
		val temp = jsonObject["tech"].asJsonObject
		val tech = context!!.deserialize<PlayerTechnology>(temp, PlayerTechnology::class.java)
		
		return Player(
			teamName = teamName,
			alias = alias,
			economy = economy,
			industry = industry,
			science = science,
			stars = stars,
			fleet = fleet,
			ships = ships,
			isActive = isActive,
			technologies = tech
		)
	}
}