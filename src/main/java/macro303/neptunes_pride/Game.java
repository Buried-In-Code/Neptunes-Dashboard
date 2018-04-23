package macro303.neptunes_pride;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by Macro303 on 2018-04-17.
 */
public class Game {
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
	@SerializedName("tick")
	private int turn;
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
	private long timeOut;

	public Object getFleets() {
		return fleets;
	}

	public double getFleetSpeed() {
		return fleetSpeed;
	}

	public boolean isPaused() {
		return paused;
	}

	public int getProductions() {
		return productions;
	}

	public int getTickFragment() {
		return tickFragment;
	}

	public long getNow() {
		return now;
	}

	public int getTickRate() {
		return tickRate;
	}

	public int getProductionRate() {
		return productionRate;
	}

	public TreeSet<Star> getStars() {
		return starSet;
	}

	public int getStarVictory() {
		return starVictory;
	}

	public int getGameOver() {
		return gameOver;
	}

	public boolean isStarted() {
		return started;
	}

	public long getStartTime() {
		return startTime;
	}

	public int getTotalStars() {
		return totalStars;
	}

	public int getProductionCounter() {
		return productionCounter;
	}

	public int getTradeScanned() {
		return tradeScanned;
	}

	public int getTurn() {
		return turn;
	}

	public int getTradeCost() {
		return tradeCost;
	}

	public String getName() {
		return name;
	}

	public int getPlayerUID() {
		return playerUID;
	}

	public int getAdmin() {
		return admin;
	}

	public int getTurnBased() {
		return turnBased;
	}

	public int getWar() {
		return war;
	}

	public TreeSet<Player> getPlayers() {
		return playerSet;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void format(){
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
		if (turn != game.turn) return false;
		if (tradeCost != game.tradeCost) return false;
		if (playerUID != game.playerUID) return false;
		if (admin != game.admin) return false;
		if (turnBased != game.turnBased) return false;
		if (war != game.war) return false;
		if (timeOut != game.timeOut) return false;
		if (fleets != null ? !fleets.equals(game.fleets) : game.fleets != null) return false;
		if (stars != null ? !stars.equals(game.stars) : game.stars != null) return false;
		if (starSet != null ? !starSet.equals(game.starSet) : game.starSet != null) return false;
		if (name != null ? !name.equals(game.name) : game.name != null) return false;
		if (players != null ? !players.equals(game.players) : game.players != null) return false;
		return playerSet != null ? playerSet.equals(game.playerSet) : game.playerSet == null;
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
		result = 31 * result + (starSet != null ? starSet.hashCode() : 0);
		result = 31 * result + starVictory;
		result = 31 * result + gameOver;
		result = 31 * result + (started ? 1 : 0);
		result = 31 * result + (int) (startTime ^ (startTime >>> 32));
		result = 31 * result + totalStars;
		result = 31 * result + productionCounter;
		result = 31 * result + tradeScanned;
		result = 31 * result + turn;
		result = 31 * result + tradeCost;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + playerUID;
		result = 31 * result + admin;
		result = 31 * result + turnBased;
		result = 31 * result + war;
		result = 31 * result + (players != null ? players.hashCode() : 0);
		result = 31 * result + (playerSet != null ? playerSet.hashCode() : 0);
		result = 31 * result + (int) (timeOut ^ (timeOut >>> 32));
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
				", starSet=" + starSet +
				", starVictory=" + starVictory +
				", gameOver=" + gameOver +
				", started=" + started +
				", startTime=" + startTime +
				", totalStars=" + totalStars +
				", productionCounter=" + productionCounter +
				", tradeScanned=" + tradeScanned +
				", turn=" + turn +
				", tradeCost=" + tradeCost +
				", name='" + name + '\'' +
				", playerUID=" + playerUID +
				", admin=" + admin +
				", turnBased=" + turnBased +
				", war=" + war +
				", players=" + players +
				", playerSet=" + playerSet +
				", timeOut=" + timeOut +
				'}';
	}
}