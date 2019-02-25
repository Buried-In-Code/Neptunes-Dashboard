package macro.neptunes.technology

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type
import kotlin.math.roundToInt

/**
 * Created by Macro303 on 2019-Feb-25.
 */
object TechnologyDeserializer : JsonDeserializer<PlayerTechnology> {

	override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): PlayerTechnology {
		val jsonObject: JsonObject = json!!.asJsonObject

		val scanningTech = jsonObject["scanning"].asJsonObject
		val scanning = Technology(name = "Scanning", level = scanningTech["level"].asDouble.roundToInt(), value = scanningTech["value"].asDouble)
		val hyperspaceTech = jsonObject["propulsion"].asJsonObject
		val hyperspace = Technology(name = "Hyperspace", level = hyperspaceTech["level"].asDouble.roundToInt(), value = hyperspaceTech["value"].asDouble)
		val terraformingTech = jsonObject["terraforming"].asJsonObject
		val terraforming = Technology(name = "Terraforming", level = terraformingTech["level"].asDouble.roundToInt(), value = terraformingTech["value"].asDouble)
		val experimentationTech = jsonObject["research"].asJsonObject
		val experimentation = Technology(name = "Experimentation", level = experimentationTech["level"].asDouble.roundToInt(), value = experimentationTech["value"].asDouble)
		val weaponsTech = jsonObject["weapons"].asJsonObject
		val weapons = Technology(name = "Weapons", level = weaponsTech["level"].asDouble.roundToInt(), value = weaponsTech["value"].asDouble)
		val bankingTech = jsonObject["banking"].asJsonObject
		val banking = Technology(name = "Banking", level = bankingTech["level"].asDouble.roundToInt(), value = bankingTech["value"].asDouble)
		val manufacturingTech = jsonObject["manufacturing"].asJsonObject
		val manufacturing = Technology(name = "Manufacturing", level = manufacturingTech["level"].asDouble.roundToInt(), value = manufacturingTech["value"].asDouble)

		return PlayerTechnology(
			scanning = scanning,
			hyperspace = hyperspace,
			terraforming = terraforming,
			experimentation = experimentation,
			weapons = weapons,
			banking = banking,
			manufacturing = manufacturing
		)
	}
}