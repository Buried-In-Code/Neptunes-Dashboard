package macro303.neptunes;

import macro303.neptunes.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by Macro303 on 2018-05-10.
 */
public class Team implements Comparable<Team> {
	@NotNull
	private final ArrayList<Player> members = new ArrayList<>();

	public boolean isLost() {
		return members.stream().allMatch(Player::isAI);
	}

	@NotNull
	public String getName() {
		return members.size() > 0 ? members.get(0).getTeam() : "Unknown";
	}

	public int getTotalShips() {
		int sum = members.stream().filter(member -> !member.isAI() && !member.isConceded()).mapToInt(Player::getTotalShips).sum();
		return sum;
	}

	public int getTotalStars() {
		return members.stream().filter(member -> !member.isAI() && !member.isConceded()).mapToInt(Player::getTotalStars).sum();
	}

	public int getTotalEconomy() {
		return members.stream().filter(member -> !member.isAI() && !member.isConceded()).mapToInt(Player::getTotalEconomy).sum();
	}

	public int getEconomyTurn() {
		return members.stream().filter(member -> !member.isAI() && !member.isConceded()).mapToInt(Player::getEconomyTurn).sum();
	}

	public int getTotalIndustry() {
		return members.stream().filter(member -> !member.isAI() && !member.isConceded()).mapToInt(Player::getTotalIndustry).sum();
	}

	public int getIndustryTurn() {
		return members.stream().filter(member -> !member.isAI() && !member.isConceded()).mapToInt(Player::getIndustryTurn).sum();
	}

	public int getTotalScience() {
		return members.stream().filter(member -> !member.isAI() && !member.isConceded()).mapToInt(Player::getTotalScience).sum();
	}

	public void addMember(Player player) {
		members.add(player);
	}

	@Override
	public int compareTo(@NotNull Team other) {
		if (getTotalStars() - other.getTotalStars() != 0)
			return getTotalStars() - other.getTotalStars();
		if (getTotalShips() - other.getTotalShips() != 0)
			return getTotalShips() - other.getTotalShips();
		if ((getTotalEconomy() + getTotalIndustry() + getTotalScience()) - (other.getTotalEconomy() + other.getTotalIndustry() + other.getTotalScience()) != 0)
			return (getTotalEconomy() + getTotalIndustry() + getTotalScience()) - (other.getTotalEconomy() + other.getTotalIndustry() + other.getTotalScience());
		return getName().compareToIgnoreCase(other.getName());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Team)) return false;

		Team team = (Team) o;

		return members.equals(team.members);
	}

	@Override
	public int hashCode() {
		return members.hashCode();
	}

	@Override
	public String toString() {
		return "Team{" +
				"members=" + members +
				'}';
	}
}