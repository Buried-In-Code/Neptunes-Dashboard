package macro303.neptunes.technology;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by Macro303 on 2018-05-10.
 */
public class TechnologyAdapter implements JsonSerializer<Technology>, JsonDeserializer<Technology> {

	@SuppressWarnings("unchecked")
	@Override
	public Technology deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		var jsonObject = json.getAsJsonObject();

		var level = jsonObject.get("level").getAsInt();
		var value = jsonObject.get("value").getAsInt();

		return new Technology(level, value);
	}

	@Override
	public JsonElement serialize(Technology src, Type typeOfSrc, JsonSerializationContext context) {
		var jsonObject = new JsonObject();

		jsonObject.addProperty("level", src.getLevel());
		jsonObject.addProperty("value", src.getValue());

		return jsonObject;
	}
}