package macro303.neptunes_pride;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by Macro303 on 2018-04-17.
 */
class Game {
	private Object fleets;
	@SerializedName("fleet_speed")
	private double fleetSpeed;
	private boolean paused;
	private int productions;
	@SerializedName("tick_fragment")
	private int tickFragment;
	private long now;
	@SerializedName("tick_rate")
	private int tickRate;
	@SerializedName("production_rate")
	private int productionRate;
	private HashMap<String, Star> stars;
	private TreeSet<Star> starSet;
	@SerializedName("stars_for_victory")
	private int starVictory;
	@SerializedName("game_over")
	private int gameOver;
	private boolean started;
	@SerializedName("start_time")
	private long startTime;
	@SerializedName("total_stars")
	private int totalStars;
	@SerializedName("production_counter")
	private int productionCounter;
	@SerializedName("trade_scanned")
	private int tradeScanned;
	private int tick;
	@SerializedName("trade_cost")
	private int tradeCost;
	private String name;
	@SerializedName("player_uid")
	private int playerUID;
	private int admin;
	@SerializedName("turn_based")
	private int turnBased;
	private int war;
	private HashMap<String, Player> players;
	private TreeSet<Player> playerSet;
	@SerializedName("turn_based_time_out")
	private int timeOut;

	Object getFleets() {
		return fleets;
	}

	double getFleetSpeed() {
		return fleetSpeed;
	}

	boolean isPaused() {
		return paused;
	}

	int getProductions() {
		return productions;
	}

	int getTickFragment() {
		return tickFragment;
	}

	long getNow() {
		return now;
	}

	int getTickRate() {
		return tickRate;
	}

	int getProductionRate() {
		return productionRate;
	}

	TreeSet<Star> getStars() {
		return starSet;
	}

	int getStarVictory() {
		return starVictory;
	}

	int getGameOver() {
		return gameOver;
	}

	boolean isStarted() {
		return started;
	}

	long getStartTime() {
		return startTime;
	}

	int getTotalStars() {
		return totalStars;
	}

	int getProductionCounter() {
		return productionCounter;
	}

	int getTradeScanned() {
		return tradeScanned;
	}

	int getTick() {
		return tick;
	}

	int getTradeCost() {
		return tradeCost;
	}

	String getName() {
		return name;
	}

	int getPlayerUID() {
		return playerUID;
	}

	int getAdmin() {
		return admin;
	}

	int getTurnBased() {
		return turnBased;
	}

	int getWar() {
		return war;
	}

	TreeSet<Player> getPlayers() {
		return playerSet;
	}

	int getTimeOut() {
		return timeOut;
	}

	void format(){
		starSet = new TreeSet<>(stars.values());
		playerSet = new TreeSet<>(players.values());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Game)) return false;

		Game game = (Game) o;

		if (Double.compare(game.fleetSpeed, fleetSpeed) != 0) return false;
		if (paused != game.paused) return false;
		if (productions != game.productions) return false;
		if (tickFragment != game.tickFragment) return false;
		if (now != game.now) return false;
		if (tickRate != game.tickRate) return false;
		if (productionRate != game.productionRate) return false;
		if (starVictory != game.starVictory) return false;
		if (gameOver != game.gameOver) return false;
		if (started != game.started) return false;
		if (startTime != game.startTime) return false;
		if (totalStars != game.totalStars) return false;
		if (productionCounter != game.productionCounter) return false;
		if (tradeScanned != game.tradeScanned) return false;
		if (tick != game.tick) return false;
		if (tradeCost != game.tradeCost) return false;
		if (playerUID != game.playerUID) return false;
		if (admin != game.admin) return false;
		if (turnBased != game.turnBased) return false;
		if (war != game.war) return false;
		if (timeOut != game.timeOut) return false;
		if (fleets != null ? !fleets.equals(game.fleets) : game.fleets != null) return false;
		if (stars != null ? !stars.equals(game.stars) : game.stars != null) return false;
		if (name != null ? !name.equals(game.name) : game.name != null) return false;
		return players != null ? players.equals(game.players) : game.players == null;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = fleets != null ? fleets.hashCode() : 0;
		temp = Double.doubleToLongBits(fleetSpeed);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (paused ? 1 : 0);
		result = 31 * result + productions;
		result = 31 * result + tickFragment;
		result = 31 * result + (int) (now ^ (now >>> 32));
		result = 31 * result + tickRate;
		result = 31 * result + productionRate;
		result = 31 * result + (stars != null ? stars.hashCode() : 0);
		result = 31 * result + starVictory;
		result = 31 * result + gameOver;
		result = 31 * result + (started ? 1 : 0);
		result = 31 * result + (int) (startTime ^ (startTime >>> 32));
		result = 31 * result + totalStars;
		result = 31 * result + productionCounter;
		result = 31 * result + tradeScanned;
		result = 31 * result + tick;
		result = 31 * result + tradeCost;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + playerUID;
		result = 31 * result + admin;
		result = 31 * result + turnBased;
		result = 31 * result + war;
		result = 31 * result + (players != null ? players.hashCode() : 0);
		result = 31 * result + timeOut;
		return result;
	}

	@Override
	public String toString() {
		return "Game{" +
				"fleets=" + fleets +
				", fleetSpeed=" + fleetSpeed +
				", paused=" + paused +
				", productions=" + productions +
				", tickFragment=" + tickFragment +
				", now=" + now +
				", tickRate=" + tickRate +
				", productionRate=" + productionRate +
				", stars=" + stars +
				", starVictory=" + starVictory +
				", gameOver=" + gameOver +
				", started=" + started +
				", startTime=" + startTime +
				", totalStars=" + totalStars +
				", productionCounter=" + productionCounter +
				", tradeScanned=" + tradeScanned +
				", tick=" + tick +
				", tradeCost=" + tradeCost +
				", name='" + name + '\'' +
				", playerUID=" + playerUID +
				", admin=" + admin +
				", turnBased=" + turnBased +
				", war=" + war +
				", players=" + players +
				", timeOut=" + timeOut +
				'}';
	}
}