package macro303.neptunes;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Macro303 on 2018-05-07.
 */
public class Player implements Comparable<Player> {
	@NotNull
	@SerializedName("ai")
	private Integer ai = -1;
	@NotNull
	@SerializedName("alias")
	private String alias = "Unknown";
	@NotNull
	@SerializedName("avatar")
	private Integer avatar = -1;
	@NotNull
	@SerializedName("conceded")
	private Integer conceded = -1;
	@NotNull
	@SerializedName("huid")
	private Integer huid = -1;
	@NotNull
	@SerializedName("karma_to_give")
	private Integer karmaToGive = -1;
	@NotNull
	@SerializedName("missed_turns")
	private Integer missedTurns = -1;
	@NotNull
	@SerializedName("uid")
	private Integer uid = -1;                            //Player ID
	@NotNull
	@SerializedName("ready")
	private Integer ready = -1;
	@NotNull
	@SerializedName("regard")
	private Integer regard = -1;
	@NotNull
	@SerializedName("tech")
	private HashMap<String, Technology> tech = new HashMap<>();
	@NotNull
	@SerializedName("total_economy")
	private Integer totalEconomy = -1;
	@NotNull
	@SerializedName("total_fleets")
	private Integer totalFleets = -1;
	@NotNull
	@SerializedName("total_industry")
	private Integer totalIndustry = -1;
	@NotNull
	@SerializedName("total_science")
	private Integer totalScience = -1;
	@NotNull
	@SerializedName("total_strength")
	private Integer totalStrength = -1;                 //Total Ships
	@NotNull
	@SerializedName("total_stars")
	private Integer totalStars = -1;

	public boolean isAi() {
		return ai != 0;
	}

	@NotNull
	public String getAlias() {
		return alias;
	}

	public boolean isConceded() {
		return conceded != 0;
	}

	@NotNull
	public String getName() {
		return Connection.getConfig().getPlayerNames().getOrDefault(alias, "Unknown");
	}

	@NotNull
	public String getTeam() {
		return Connection.getConfig().getTeams().entrySet().stream().filter(entry -> entry.getValue().contains(getName())).findFirst().map(Map.Entry::getKey).orElse("Unknown");
	}

	@NotNull
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Player)) return false;

		Player player = (Player) o;

		if (!ai.equals(player.ai)) return false;
		if (!alias.equals(player.alias)) return false;
		if (!avatar.equals(player.avatar)) return false;
		if (!conceded.equals(player.conceded)) return false;
		if (!huid.equals(player.huid)) return false;
		if (!karmaToGive.equals(player.karmaToGive)) return false;
		if (!missedTurns.equals(player.missedTurns)) return false;
		if (!uid.equals(player.uid)) return false;
		if (!ready.equals(player.ready)) return false;
		if (!regard.equals(player.regard)) return false;
		if (!tech.equals(player.tech)) return false;
		if (!totalEconomy.equals(player.totalEconomy)) return false;
		if (!totalFleets.equals(player.totalFleets)) return false;
		if (!totalIndustry.equals(player.totalIndustry)) return false;
		if (!totalScience.equals(player.totalScience)) return false;
		if (!totalStrength.equals(player.totalStrength)) return false;
		return totalStars.equals(player.totalStars);
	}

	@Override
	public int hashCode() {
		int result = ai.hashCode();
		result = 31 * result + alias.hashCode();
		result = 31 * result + avatar.hashCode();
		result = 31 * result + conceded.hashCode();
		result = 31 * result + huid.hashCode();
		result = 31 * result + karmaToGive.hashCode();
		result = 31 * result + missedTurns.hashCode();
		result = 31 * result + uid.hashCode();
		result = 31 * result + ready.hashCode();
		result = 31 * result + regard.hashCode();
		result = 31 * result + tech.hashCode();
		result = 31 * result + totalEconomy.hashCode();
		result = 31 * result + totalFleets.hashCode();
		result = 31 * result + totalIndustry.hashCode();
		result = 31 * result + totalScience.hashCode();
		result = 31 * result + totalStrength.hashCode();
		result = 31 * result + totalStars.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Player{" +
				"ai=" + ai +
				", alias='" + alias + '\'' +
				", avatar=" + avatar +
				", conceded=" + conceded +
				", huid=" + huid +
				", karmaToGive=" + karmaToGive +
				", missedTurns=" + missedTurns +
				", uid=" + uid +
				", ready=" + ready +
				", regard=" + regard +
				", tech=" + tech +
				", totalEconomy=" + totalEconomy +
				", totalFleets=" + totalFleets +
				", totalIndustry=" + totalIndustry +
				", totalScience=" + totalScience +
				", totalStrength=" + totalStrength +
				", totalStars=" + totalStars +
				'}';
	}
}