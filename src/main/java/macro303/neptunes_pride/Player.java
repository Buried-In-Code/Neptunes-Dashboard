package macro303.neptunes_pride;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-04-17.
 */
class Player implements Comparable<Player> {
	@SerializedName("total_industry")
	private int totalIndustry = 0;
	private int regard = 0;
	@SerializedName("total_science")
	private int totalScience = 0;
	private int uid = 0;
	private int ai = 0;
	private int huid = 0;
	@SerializedName("total_stars")
	private int totalStars = 0;
	@SerializedName("total_fleets")
	private int totalFleets = 0;
	@SerializedName("total_strength")
	private int totalStrength = 0;
	private String alias = null;
	@SerializedName("tech")
	private PlayerTechnology playerTechnology = null;
	private int avatar = 0;
	private int conceded = 0;
	private int ready = 0;
	@SerializedName("total_economy")
	private int totalEconomy = 0;
	@SerializedName("missed_turns")
	private int missedTurns = 0;
	@SerializedName("karma_to_give")
	private int karma = 0;

	int getTotalIndustry() {
		return totalIndustry;
	}

	int getRegard() {
		return regard;
	}

	int getTotalScience() {
		return totalScience;
	}

	int getUid() {
		return uid;
	}

	int getAi() {
		return ai;
	}

	int getHuid() {
		return huid;
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

	PlayerTechnology getPlayerTechnology() {
		return playerTechnology;
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

	@Override
	public int compareTo(@NotNull Player other) {
		if (totalStrength - other.totalStrength != 0)
			return totalStrength - other.totalStrength;
		if (playerTechnology.compareTo(other.playerTechnology) != 0)
			return playerTechnology.compareTo(other.playerTechnology);
		if (totalStars - other.totalStars != 0)
			return totalStars - other.totalStars;
		if (totalFleets - other.totalFleets != 0)
			return totalFleets - other.totalFleets;
		if (getTotalStats() - other.getTotalStats() != 0)
			return getTotalStats() - other.getTotalStats();
		return alias.compareToIgnoreCase(other.alias);
	}
}