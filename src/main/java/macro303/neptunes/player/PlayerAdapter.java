package macro303.neptunes.player;

import com.google.gson.*;
import macro303.neptunes.technology.Technology;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Macro303 on 2018-05-10.
 */
public class PlayerAdapter implements JsonSerializer<Player>, JsonDeserializer<Player> {

	@SuppressWarnings("unchecked")
	@Override
	public Player deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		var obj = json.getAsJsonObject();

		var AI = obj.get("ai").getAsInt() != 0;
		var alias = obj.get("alias").getAsString();
		var conceded = obj.get("conceded").getAsInt() != 0;
		var techMap = obj.get("tech").getAsJsonObject();
		var technologies = new HashMap<String, Technology>();
		for (Map.Entry<String, JsonElement> tech : techMap.entrySet()) {
			technologies.put(tech.getKey(), context.deserialize(tech.getValue(), Technology.class));
		}
		var totalEconomy = obj.get("total_economy").getAsInt();
		var totalFleets = obj.get("total_fleets").getAsInt();
		var totalIndustry = obj.get("total_industry").getAsInt();
		var totalScience = obj.get("total_science").getAsInt();
		var totalShips = obj.get("total_strength").getAsInt();
		var totalStars = obj.get("total_stars").getAsInt();

		return new Player(AI, alias, conceded, technologies, totalEconomy, totalFleets, totalIndustry, totalScience, totalShips, totalStars);
	}

	@Override
	public JsonElement serialize(Player src, Type typeOfSrc, JsonSerializationContext context) {
		var obj = new JsonObject();

		obj.addProperty("AI", src.isAI());
		obj.addProperty("alias", src.getAlias());
		obj.addProperty("conceded", src.isConceded());
		var technologies = context.serialize(src.getTechnologies());
		obj.add("technologies", technologies);
		obj.addProperty("totalEconomy", src.getTotalEconomy());
		obj.addProperty("totalFleets", src.getTotalFleets());
		obj.addProperty("totalIndustry", src.getTotalIndustry());
		obj.addProperty("totalScience", src.getTotalScience());
		obj.addProperty("totalShips", src.getTotalShips());
		obj.addProperty("totalStars", src.getTotalStars());

		return obj;
	}
}