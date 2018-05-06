package macro303.neptunes;

import com.google.gson.annotations.SerializedName;
import macro303.neptunes_pride.technology.Technology;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player implements Comparable<Player> {
	@SerializedName("ai")
	private Integer ai;
	@SerializedName("alias")
	private String alias;
	@SerializedName("avatar")
	private Integer avatar;
	@SerializedName("conceded")
	private Integer conceded;
	@SerializedName("huid")
	private Integer huid;
	@SerializedName("karma_to_give")
	private Integer karmaToGive;
	@SerializedName("missed_turns")
	private Integer missedTurns;
	@SerializedName("uid")
	private Integer uid;
	@SerializedName("ready")
	private Integer ready;
	@SerializedName("regard")
	private Integer regard;
	@SerializedName("tech")
	private HashMap<String, Technology> tech;
	@SerializedName("total_economy")
	private Integer totalEconomy;
	@SerializedName("total_fleets")
	private Integer totalFleets;
	@SerializedName("total_industry")
	private Integer totalIndustry;
	@SerializedName("total_science")
	private Integer totalScience;
	@SerializedName("total_strength")
	private Integer totalStrength;
	@SerializedName("total_stars")
	private Integer totalStars;

	public boolean isAi() {
		return ai != 0;
	}

	public String getAlias() {
		return alias;
	}

	public boolean isConceded() {
		return conceded != 0;
	}

	public String getName() {
		return Connection.getConfig().getPlayerNames().getOrDefault(alias, "Unknown");
	}

	public String getTeam() {
		for (Map.Entry<String, ArrayList<String>> entry : Connection.getConfig().getTeams().entrySet())
			if (entry.getValue().contains(getName()))
				return entry.getKey();
		return "Unknown";
	}

	public HashMap<String, Technology> getTech() {
		return tech;
	}

	public int getTotalEconomy() {
		return totalEconomy;
	}

	public int getTotalFleets() {
		return totalFleets;
	}

	public int getTotalIndustry() {
		return totalIndustry;
	}

	public int getTotalScience() {
		return totalScience;
	}

	public int getTotalStrength() {
		return totalStrength;
	}

	public int getTotalStars() {
		return totalStars;
	}

	@Override
	public int compareTo(@NotNull Player other) {
		if (totalStars.compareTo(other.totalStars) != 0)
			return totalStars.compareTo(other.totalStars);
		if (totalStrength.compareTo(other.totalStrength) != 0)
			return totalStrength.compareTo(other.totalStrength);
		if ((totalEconomy + totalIndustry + totalScience) - (other.totalEconomy + other.totalIndustry + other.totalScience) != 0)
			return (totalEconomy + totalIndustry + totalScience) - (other.totalEconomy + other.totalIndustry + other.totalScience);
		if (tech.values().stream().mapToInt(Technology::getLevel).sum() - other.tech.values().stream().mapToInt(Technology::getLevel).sum() != 0)
			return tech.values().stream().mapToInt(Technology::getLevel).sum() - other.tech.values().stream().mapToInt(Technology::getLevel).sum();
		if (totalFleets.compareTo(other.totalFleets) != 0)
			return totalFleets.compareTo(other.totalFleets);
		return alias.compareToIgnoreCase(other.alias);
	}
}
