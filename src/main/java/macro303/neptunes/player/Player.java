package macro303.neptunes.player;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import macro303.neptunes.technology.Technology;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by Macro303 on 2018-05-07.
 */
public class Player implements Comparable<Player> {
	@NotNull
	private final BooleanProperty AI;
	@NotNull
	private final StringProperty alias;
	@NotNull
	private final BooleanProperty conceded;
	@NotNull
	private final StringProperty name;
	@NotNull
	private final StringProperty team;
	@NotNull
	private final ObservableMap<String, Technology> technologies;
	@NotNull
	private final IntegerProperty totalEconomy;
	@NotNull
	private final IntegerProperty totalIndustry;
	@NotNull
	private final IntegerProperty totalScience;
	@NotNull
	private final IntegerProperty totalShips;
	@NotNull
	private final IntegerProperty totalStars;

	public Player(boolean AI, @NotNull String alias, boolean conceded, @NotNull String name, @NotNull String team, @NotNull Map<String, Technology> technologies, int totalEconomy, int totalIndustry, int totalScience, int totalShips, int totalStars) {
		this.AI = new SimpleBooleanProperty(AI);
		this.alias = new SimpleStringProperty(alias);
		this.conceded = new SimpleBooleanProperty(conceded);
		this.name = new SimpleStringProperty(name);
		this.team = new SimpleStringProperty(team);
		this.technologies = FXCollections.observableMap(technologies);
		this.totalEconomy = new SimpleIntegerProperty(totalEconomy);
		this.totalIndustry = new SimpleIntegerProperty(totalIndustry);
		this.totalScience = new SimpleIntegerProperty(totalScience);
		this.totalShips = new SimpleIntegerProperty(totalShips);
		this.totalStars = new SimpleIntegerProperty(totalStars);
	}

	public boolean isAI() {
		return AI.get();
	}

	public void setAI(boolean AI) {
		this.AI.set(AI);
	}

	public @NotNull BooleanProperty AIProperty() {
		return AI;
	}

	public String getAlias() {
		return alias.get();
	}

	public void setAlias(String alias) {
		this.alias.set(alias);
	}

	public @NotNull StringProperty aliasProperty() {
		return alias;
	}

	public boolean isConceded() {
		return conceded.get();
	}

	public void setConceded(boolean conceded) {
		this.conceded.set(conceded);
	}

	public @NotNull BooleanProperty concededProperty() {
		return conceded;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public @NotNull StringProperty nameProperty() {
		return name;
	}

	public String getTeam() {
		return team.get();
	}

	public void setTeam(String team) {
		this.team.set(team);
	}

	public @NotNull StringProperty teamProperty() {
		return team;
	}

	public ObservableMap<String, Technology> getTechnologies() {
		return technologies;
	}

	public int getTotalEconomy() {
		return totalEconomy.get();
	}

	public void setTotalEconomy(int totalEconomy) {
		this.totalEconomy.set(totalEconomy);
	}

	public @NotNull IntegerProperty totalEconomyProperty() {
		return totalEconomy;
	}

	public int getTotalIndustry() {
		return totalIndustry.get();
	}

	public void setTotalIndustry(int totalIndustry) {
		this.totalIndustry.set(totalIndustry);
	}

	public @NotNull IntegerProperty totalIndustryProperty() {
		return totalIndustry;
	}

	public int getTotalScience() {
		return totalScience.get();
	}

	public void setTotalScience(int totalScience) {
		this.totalScience.set(totalScience);
	}

	public @NotNull IntegerProperty totalScienceProperty() {
		return totalScience;
	}

	public int getTotalShips() {
		return totalShips.get();
	}

	public void setTotalShips(int totalShips) {
		this.totalShips.set(totalShips);
	}

	public @NotNull IntegerProperty totalShipsProperty() {
		return totalShips;
	}

	public int getTotalStars() {
		return totalStars.get();
	}

	public void setTotalStars(int totalStars) {
		this.totalStars.set(totalStars);
	}

	public @NotNull IntegerProperty totalStarsProperty() {
		return totalStars;
	}

	public int getEconomyTurn() {
		return (getTotalEconomy() * 10) + (getTechnologies().get("banking").getLevel() * 75);
	}

	public int getIndustryTurn() {
		return getTotalIndustry() * (getTechnologies().get("manufacturing").getLevel() + 5) / 24;
	}

	@Override
	public int compareTo(@NotNull Player other) {
		if (getTotalStars() - other.getTotalStars() != 0)
			return getTotalStars() - other.getTotalStars();
		if (getTotalShips() - other.getTotalShips() != 0)
			return getTotalShips() - other.getTotalShips();
		if ((getTotalEconomy() + getTotalIndustry() + getTotalScience()) - (other.getTotalEconomy() + other.getTotalIndustry() + other.getTotalScience()) != 0)
			return (getTotalEconomy() + getTotalIndustry() + getTotalScience()) - (other.getTotalEconomy() + other.getTotalIndustry() + other.getTotalScience());
		if (getTechnologies().values().stream().mapToInt(Technology::getLevel).sum() - other.getTechnologies().values().stream().mapToInt(Technology::getLevel).sum() != 0)
			return getTechnologies().values().stream().mapToInt(Technology::getLevel).sum() - other.getTechnologies().values().stream().mapToInt(Technology::getLevel).sum();
		return getAlias().compareToIgnoreCase(other.getAlias());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Player)) return false;

		Player player = (Player) o;

		if (!AI.equals(player.AI)) return false;
		if (!alias.equals(player.alias)) return false;
		if (!conceded.equals(player.conceded)) return false;
		if (!name.equals(player.name)) return false;
		if (!team.equals(player.team)) return false;
		if (!technologies.equals(player.technologies)) return false;
		if (!totalEconomy.equals(player.totalEconomy)) return false;
		if (!totalIndustry.equals(player.totalIndustry)) return false;
		if (!totalScience.equals(player.totalScience)) return false;
		if (!totalShips.equals(player.totalShips)) return false;
		return totalStars.equals(player.totalStars);
	}

	@Override
	public int hashCode() {
		int result = AI.hashCode();
		result = 31 * result + alias.hashCode();
		result = 31 * result + conceded.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + team.hashCode();
		result = 31 * result + technologies.hashCode();
		result = 31 * result + totalEconomy.hashCode();
		result = 31 * result + totalIndustry.hashCode();
		result = 31 * result + totalScience.hashCode();
		result = 31 * result + totalShips.hashCode();
		result = 31 * result + totalStars.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Player{" +
				"AI=" + AI +
				", alias=" + alias +
				", conceded=" + conceded +
				", name=" + name +
				", team=" + team +
				", technologies=" + technologies +
				", totalEconomy=" + totalEconomy +
				", totalIndustry=" + totalIndustry +
				", totalScience=" + totalScience +
				", totalShips=" + totalShips +
				", totalStars=" + totalStars +
				'}';
	}
}