package macro303.neptunes;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by Macro303 on 2018-05-07.
 */
public class Game {
	@NotNull
	@SerializedName("admin")
	private Integer admin = -1;
	@Nullable
	@SerializedName("fleets")
	private Object fleets = null;
	@NotNull
	@SerializedName("fleet_speed")
	private Double fleetSpeed = -1.0;
	@NotNull
	@SerializedName("game_over")
	private Integer gameOver = -1;
	@NotNull
	@SerializedName("name")
	private String name = "Unknown";
	@NotNull
	@SerializedName("now")
	private Long now = -1L;
	@NotNull
	@SerializedName("paused")
	private Boolean paused = false;
	@NotNull
	@SerializedName("players")
	private HashMap<String, Player> players = new HashMap<>();
	@NotNull
	@SerializedName("player_uid")
	private Integer playerUID = -1;
	@NotNull
	@SerializedName("production_counter")
	private Integer productionCounter = -1;
	@NotNull
	@SerializedName("production_rate")
	private Integer productionRate = -1;
	@NotNull
	@SerializedName("productions")
	private Integer productions = -1;
	@NotNull
	@SerializedName("stars")
	private HashMap<String, Star> stars = new HashMap<>();
	@NotNull
	@SerializedName("stars_for_victory")
	private Integer starsForVictory = -1;
	@NotNull
	@SerializedName("start_time")
	private Long startTime = -1L;
	@NotNull
	@SerializedName("started")
	private Boolean started = false;
	@NotNull
	@SerializedName("tick")
	private Integer tick = -1;
	@NotNull
	@SerializedName("tick_fragment")
	private Integer tickFragment = -1;
	@NotNull
	@SerializedName("tick_rate")
	private Integer tickRate = -1;
	@NotNull
	@SerializedName("total_stars")
	private Integer totalStars = -1;
	@NotNull
	@SerializedName("trade_cost")
	private Integer tradeCost = -1;
	@NotNull
	@SerializedName("trade_scanned")
	private Integer tradeScanned = -1;
	@NotNull
	@SerializedName("turn_based")
	private Integer turnBased = -1;
	@NotNull
	@SerializedName("turn_based_time_out")
	private Long turnBasedTimeOut = -1L;
	@NotNull
	@SerializedName("war")
	private Integer war = -1;

	public boolean isGameOver() {
		return gameOver == 1;
	}

	@NotNull
	public String getName() {
		return name;
	}

	public boolean isPaused() {
		return paused;
	}

	@NotNull
	public TreeSet<Player> getPlayers() {
		return new TreeSet<>(players.values());
	}

	public int getProductionRate() {
		return productionRate;
	}

	public int getStarsForVictory() {
		return starsForVictory;
	}

	@NotNull
	public LocalDateTime getStartTime() {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault());
	}

	public boolean isStarted() {
		return started;
	}

	public int getTick() {
		return tick;
	}

	public int getTotalStars() {
		return totalStars;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Game)) return false;

		Game game = (Game) o;

		if (!admin.equals(game.admin)) return false;
		if (fleets != null ? !fleets.equals(game.fleets) : game.fleets != null) return false;
		if (!fleetSpeed.equals(game.fleetSpeed)) return false;
		if (!gameOver.equals(game.gameOver)) return false;
		if (!name.equals(game.name)) return false;
		if (!now.equals(game.now)) return false;
		if (!paused.equals(game.paused)) return false;
		if (!players.equals(game.players)) return false;
		if (!playerUID.equals(game.playerUID)) return false;
		if (!productionCounter.equals(game.productionCounter)) return false;
		if (!productionRate.equals(game.productionRate)) return false;
		if (!productions.equals(game.productions)) return false;
		if (!stars.equals(game.stars)) return false;
		if (!starsForVictory.equals(game.starsForVictory)) return false;
		if (!startTime.equals(game.startTime)) return false;
		if (!started.equals(game.started)) return false;
		if (!tick.equals(game.tick)) return false;
		if (!tickFragment.equals(game.tickFragment)) return false;
		if (!tickRate.equals(game.tickRate)) return false;
		if (!totalStars.equals(game.totalStars)) return false;
		if (!tradeCost.equals(game.tradeCost)) return false;
		if (!tradeScanned.equals(game.tradeScanned)) return false;
		if (!turnBased.equals(game.turnBased)) return false;
		if (!turnBasedTimeOut.equals(game.turnBasedTimeOut)) return false;
		return war.equals(game.war);
	}

	@Override
	public int hashCode() {
		int result = admin.hashCode();
		result = 31 * result + (fleets != null ? fleets.hashCode() : 0);
		result = 31 * result + fleetSpeed.hashCode();
		result = 31 * result + gameOver.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + now.hashCode();
		result = 31 * result + paused.hashCode();
		result = 31 * result + players.hashCode();
		result = 31 * result + playerUID.hashCode();
		result = 31 * result + productionCounter.hashCode();
		result = 31 * result + productionRate.hashCode();
		result = 31 * result + productions.hashCode();
		result = 31 * result + stars.hashCode();
		result = 31 * result + starsForVictory.hashCode();
		result = 31 * result + startTime.hashCode();
		result = 31 * result + started.hashCode();
		result = 31 * result + tick.hashCode();
		result = 31 * result + tickFragment.hashCode();
		result = 31 * result + tickRate.hashCode();
		result = 31 * result + totalStars.hashCode();
		result = 31 * result + tradeCost.hashCode();
		result = 31 * result + tradeScanned.hashCode();
		result = 31 * result + turnBased.hashCode();
		result = 31 * result + turnBasedTimeOut.hashCode();
		result = 31 * result + war.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Game{" +
				"admin=" + admin +
				", fleets=" + fleets +
				", fleetSpeed=" + fleetSpeed +
				", gameOver=" + gameOver +
				", name='" + name + '\'' +
				", now=" + now +
				", paused=" + paused +
				", players=" + players +
				", playerUID=" + playerUID +
				", productionCounter=" + productionCounter +
				", productionRate=" + productionRate +
				", productions=" + productions +
				", stars=" + stars +
				", starsForVictory=" + starsForVictory +
				", startTime=" + startTime +
				", started=" + started +
				", tick=" + tick +
				", tickFragment=" + tickFragment +
				", tickRate=" + tickRate +
				", totalStars=" + totalStars +
				", tradeCost=" + tradeCost +
				", tradeScanned=" + tradeScanned +
				", turnBased=" + turnBased +
				", turnBasedTimeOut=" + turnBasedTimeOut +
				", war=" + war +
				'}';
	}
}