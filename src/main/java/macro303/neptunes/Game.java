package macro303.neptunes;

import com.google.gson.annotations.SerializedName;
import macro303.neptunes_pride.player.Player;
import macro303.neptunes_pride.star.Star;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.TreeSet;

public class Game {
	@SerializedName("admin")
	private Integer admin;
	@SerializedName("fleets")
	private Object fleets;
	@SerializedName("fleet_speed")
	private Double fleetSpeed;
	@SerializedName("game_over")
	private Integer gameOver;
	@SerializedName("name")
	private String name;
	@SerializedName("now")
	private Long now;
	@SerializedName("paused")
	private Boolean paused;
	@SerializedName("players")
	private HashMap<String, Player> players;
	@SerializedName("player_uid")
	private Integer playerUID;
	@SerializedName("production_counter")
	private Integer productionCounter;
	@SerializedName("production_rate")
	private Integer productionRate;
	@SerializedName("productions")
	private Integer productions;
	@SerializedName("stars")
	private HashMap<String, Star> stars;
	@SerializedName("stars_for_victory")
	private Integer starsForVictory;
	@SerializedName("start_time")
	private Long start_time;
	@SerializedName("started")
	private Boolean started;
	@SerializedName("tick")
	private Integer tick;
	@SerializedName("tick_fragment")
	private Integer tickFragment;
	@SerializedName("tick_rate")
	private Integer tickRate;
	@SerializedName("total_stars")
	private Integer totalStars;
	@SerializedName("trade_cost")
	private Integer tradeCost;
	@SerializedName("trade_scanned")
	private Integer tradeScanned;
	@SerializedName("turn_based")
	private Integer turnBased;
	@SerializedName("turn_based_time_out")
	private Long turnBasedTimeOut;
	@SerializedName("war")
	private Integer war;

	public boolean isGameOver() {
		return gameOver == 1;
	}

	public String getName() {
		return name;
	}

	public boolean isPaused() {
		return paused;
	}

	public TreeSet<Player> getPlayers() {
		return new TreeSet<>(players.values());
	}

	public int getProductionRate() {
		return productionRate;
	}

	public int getStarsForVictory() {
		return starsForVictory;
	}

	public LocalDateTime getStart_time() {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(start_time), ZoneId.systemDefault());
	}

	public boolean isStarted() {
		return started;
	}

	public int getTick() {
		return tick;
	}
}
