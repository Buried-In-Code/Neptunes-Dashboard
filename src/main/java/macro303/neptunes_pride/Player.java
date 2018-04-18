package macro303.neptunes_pride;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Created by Macro303 on 2018-04-17.
 */
class Player implements Comparable<Player> {
	@SerializedName("total_industry")
	private int totalIndustry;
	private int regard;
	@SerializedName("total_science")
	private int totalScience;
	@SerializedName("uid")
	private int playerID;
	@SerializedName("ai")
	private int AI;
	@SerializedName("huid")
	private int HUID;
	@SerializedName("total_stars")
	private int totalStars;
	@SerializedName("total_fleets")
	private int totalFleets;
	@SerializedName("total_strength")
	private int totalStrength;
	private String alias;
	@SerializedName("tech")
	private HashMap<String, Technology> technologyMap;
	private int avatar;
	private int conceded;
	private int ready;
	@SerializedName("total_economy")
	private int totalEconomy;
	@SerializedName("missed_turns")
	private int missedTurns;
	@SerializedName("karma_to_give")
	private int karma;

	int getTotalIndustry() {
		return totalIndustry;
	}

	int getRegard() {
		return regard;
	}

	int getTotalScience() {
		return totalScience;
	}

	int getPlayerID() {
		return playerID;
	}

	int getAI() {
		return AI;
	}

	int getHUID() {
		return HUID;
	}

	int getTotalStars() {
		return totalStars;
	}

	int getTotalFleets() {
		return totalFleets;
	}

	int getTotalStrength() {
		return totalStrength;
	}

	String getAlias() {
		return alias;
	}

	HashMap<String, Technology> getTechnologyMap() {
		return technologyMap;
	}

	int getAvatar() {
		return avatar;
	}

	int getConceded() {
		return conceded;
	}

	int getReady() {
		return ready;
	}

	int getTotalEconomy() {
		return totalEconomy;
	}

	int getMissedTurns() {
		return missedTurns;
	}

	int getKarma() {
		return karma;
	}

	int getTotalStats() {
		return totalEconomy + totalIndustry + totalScience;
	}

	private int countTechLevels() {
		return technologyMap.values().stream().mapToInt(Technology::getLevel).sum();
	}

	@Override
	public int compareTo(@NotNull Player other) {
		if (totalStrength - other.totalStrength != 0)
			return totalStrength - other.totalStrength;
		if (countTechLevels() - other.countTechLevels() != 0)
			return countTechLevels() - other.countTechLevels();
		if (totalStars - other.totalStars != 0)
			return totalStars - other.totalStars;
		if (totalFleets - other.totalFleets != 0)
			return totalFleets - other.totalFleets;
		if (getTotalStats() - other.getTotalStats() != 0)
			return getTotalStats() - other.getTotalStats();
		return alias.compareToIgnoreCase(other.alias);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Player)) return false;

		Player player = (Player) o;

		if (totalIndustry != player.totalIndustry) return false;
		if (regard != player.regard) return false;
		if (totalScience != player.totalScience) return false;
		if (playerID != player.playerID) return false;
		if (AI != player.AI) return false;
		if (HUID != player.HUID) return false;
		if (totalStars != player.totalStars) return false;
		if (totalFleets != player.totalFleets) return false;
		if (totalStrength != player.totalStrength) return false;
		if (avatar != player.avatar) return false;
		if (conceded != player.conceded) return false;
		if (ready != player.ready) return false;
		if (totalEconomy != player.totalEconomy) return false;
		if (missedTurns != player.missedTurns) return false;
		if (karma != player.karma) return false;
		if (alias != null ? !alias.equals(player.alias) : player.alias != null) return false;
		return technologyMap != null ? technologyMap.equals(player.technologyMap) : player.technologyMap == null;
	}

	@Override
	public int hashCode() {
		int result = totalIndustry;
		result = 31 * result + regard;
		result = 31 * result + totalScience;
		result = 31 * result + playerID;
		result = 31 * result + AI;
		result = 31 * result + HUID;
		result = 31 * result + totalStars;
		result = 31 * result + totalFleets;
		result = 31 * result + totalStrength;
		result = 31 * result + (alias != null ? alias.hashCode() : 0);
		result = 31 * result + (technologyMap != null ? technologyMap.hashCode() : 0);
		result = 31 * result + avatar;
		result = 31 * result + conceded;
		result = 31 * result + ready;
		result = 31 * result + totalEconomy;
		result = 31 * result + missedTurns;
		result = 31 * result + karma;
		return result;
	}

	@Override
	public String toString() {
		return "Player{" +
				"totalIndustry=" + totalIndustry +
				", regard=" + regard +
				", totalScience=" + totalScience +
				", playerID=" + playerID +
				", AI=" + AI +
				", HUID=" + HUID +
				", totalStars=" + totalStars +
				", totalFleets=" + totalFleets +
				", totalStrength=" + totalStrength +
				", alias='" + alias + '\'' +
				", technologyMap=" + technologyMap +
				", avatar=" + avatar +
				", conceded=" + conceded +
				", ready=" + ready +
				", totalEconomy=" + totalEconomy +
				", missedTurns=" + missedTurns +
				", karma=" + karma +
				'}';
	}
}