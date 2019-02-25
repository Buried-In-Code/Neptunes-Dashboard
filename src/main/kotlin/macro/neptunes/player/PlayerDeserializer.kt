package macro.neptunes.player

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import macro.neptunes.GeneralException
import macro.neptunes.team.TeamTable
import macro.neptunes.technology.PlayerTechnology
import macro.neptunes.technology.Technology
import macro.neptunes.technology.TechnologyTable
import java.lang.reflect.Type
import kotlin.math.roundToInt

/**
 * Created by Macro303 on 2019-Feb-12.
 */
object PlayerDeserializer : JsonDeserializer<Player> {

	override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Player? {
		val jsonObject: JsonObject = json!!.asJsonObject

		val teamName = "Free For All"
		var team = TeamTable.select(name = teamName)
		if (team == null) {
			TeamTable.insert(name = teamName)
			team = TeamTable.select(teamName)!!
		}
		val alias = jsonObject["alias"].asString
		if (alias.isNullOrBlank()) return null
		val economy = jsonObject["total_economy"].asInt
		val industry = jsonObject["total_industry"].asInt
		val science = jsonObject["total_science"].asInt
		val stars = jsonObject["total_stars"].asInt
		val fleet = jsonObject["total_fleets"].asInt
		val ships = jsonObject["total_strength"].asInt
		val isActive = jsonObject["conceded"].asInt == 0

		//Technology
		val temp = jsonObject["tech"].asJsonObject
		
		val exists = PlayerTable.insert(
			team = team,
			alias = alias,
			economy = economy,
			industry = industry,
			science = science,
			stars = stars,
			fleet = fleet,
			ships = ships,
			isActive = isActive
		)
		if(!exists)
			throw GeneralException()
		val player = PlayerTable.select(alias = alias)!!

		val scanningTech = temp["scanning"].asJsonObject
		val scanning = TechnologyTable.insert(player = player, name = "Scanning", level = scanningTech["level"].asDouble.roundToInt(), value = scanningTech["value"].asDouble)
		val hyperspaceTech = temp["propulsion"].asJsonObject
		val hyperspace = TechnologyTable.insert(player = player, name = "Hyperspace", level = hyperspaceTech["level"].asDouble.roundToInt(), value = hyperspaceTech["value"].asDouble)
		val terraformingTech = temp["terraforming"].asJsonObject
		val terraforming = TechnologyTable.insert(player = player, name = "Terraforming", level = terraformingTech["level"].asDouble.roundToInt(), value = terraformingTech["value"].asDouble)
		val experimentationTech = temp["research"].asJsonObject
		val experimentation = TechnologyTable.insert(player = player, name = "Experimentation", level = experimentationTech["level"].asDouble.roundToInt(), value = experimentationTech["value"].asDouble)
		val weaponsTech = temp["weapons"].asJsonObject
		val weapons = TechnologyTable.insert(player = player, name = "Weapons", level = weaponsTech["level"].asDouble.roundToInt(), value = weaponsTech["value"].asDouble)
		val bankingTech = temp["banking"].asJsonObject
		val banking = TechnologyTable.insert(player = player, name = "Banking", level = bankingTech["level"].asDouble.roundToInt(), value = bankingTech["value"].asDouble)
		val manufacturingTech = temp["manufacturing"].asJsonObject
		val manufacturing = TechnologyTable.insert(player = player, name = "Manufacturing", level = manufacturingTech["level"].asDouble.roundToInt(), value = manufacturingTech["value"].asDouble)

		return player
	}
}