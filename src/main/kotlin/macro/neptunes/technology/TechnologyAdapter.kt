package macro.neptunes.technology

import com.google.gson.*

import java.lang.reflect.Type

/**
 * Created by Macro303 on 2018-05-10.
 */
class TechnologyAdapter : JsonSerializer<Technology>, JsonDeserializer<Technology> {

	@Throws(JsonParseException::class)
	override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Technology {
		val jsonObject = json.asJsonObject

		val level = jsonObject.get("level").asInt
		val value = jsonObject.get("value").asDouble

		return Technology(level, value)
	}

	override fun serialize(src: Technology, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
		val jsonObject = JsonObject()

		jsonObject.addProperty("level", src.level)
		jsonObject.addProperty("value", src.value)

		return jsonObject
	}
}