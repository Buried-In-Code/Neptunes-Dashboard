package macro303.neptunes.game;

import com.google.gson.*;
import macro303.neptunes.Team;
import macro303.neptunes.player.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Macro303 on 2018-05-10.
 */
public class GameAdapter implements JsonSerializer<Game>, JsonDeserializer<Game> {
	@NotNull
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd HH:mm");

	@SuppressWarnings("unchecked")
	@Override
	public Game deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		var obj = json.getAsJsonObject();

		var gameOver = obj.get("game_over").getAsInt() == 1;
		var name = obj.get("name").getAsString();
		var paused = obj.get("paused").getAsBoolean();
		var playerMap = obj.get("players").getAsJsonObject();
		var players = new ArrayList<Player>();
		for (Map.Entry<String, JsonElement> player : playerMap.entrySet()) {
			players.add(context.deserialize(player.getValue(), Player.class));
		}
		var teams = new ArrayList<Team>();
		for (Player player : players) {
			Team team = new Team();
			for (Team temp : teams) {
				if (temp.getName().equalsIgnoreCase(player.getTeam()))
					team = temp;
			}
			team.addMember(player);
			if (!teams.contains(team))
				teams.add(team);
		}
		var started = obj.get("started").getAsBoolean();
		var startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(obj.get("start_time").getAsLong()), ZoneId.systemDefault());
		var victory = obj.get("stars_for_victory").getAsInt();
		var totalStars = obj.get("total_stars").getAsInt();
		var turnTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(obj.get("turn_based_time_out").getAsLong()), ZoneId.systemDefault());

		return new Game(gameOver, name, paused, players, started, startTime, teams, totalStars, turnTime, victory);
	}

	@Override
	public JsonElement serialize(Game src, Type typeOfSrc, JsonSerializationContext context) {
		var obj = new JsonObject();

		obj.addProperty("gameOver", src.isGameOver());
		obj.addProperty("name", src.getName());
		obj.addProperty("paused", src.isPaused());
		var players = context.serialize(src.getPlayers());
		obj.add("players", players);
		obj.addProperty("started", src.isStarted());
		obj.addProperty("startTime", src.getStartTime().format(formatter));
		obj.addProperty("victory", src.getVictory());
		obj.addProperty("totalStars", src.getTotalStars());

		return obj;
	}
}