package macro.neptunes.player

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import macro.neptunes.technology.Technology
import tornadofx.*

/**
 * Created by Macro303 on 2018-05-07.
 */
class Player(
	ai: Boolean,
	alias: String,
	conceded: Boolean,
	name: String,
	team: String,
	technologies: Map<String, Technology>,
	totalEconomy: Int,
	totalIndustry: Int,
	totalScience: Int,
	totalShips: Int,
	totalStars: Int
) : Comparable<Player> {
	val aiProperty = SimpleBooleanProperty(ai)
	var ai by aiProperty

	val aliasProperty = SimpleStringProperty(alias)
	var alias by aliasProperty

	val concededProperty = SimpleBooleanProperty(conceded)
	var conceded by concededProperty

	val nameProperty = SimpleStringProperty(name)
	var name by nameProperty

	val teamProperty = SimpleStringProperty(team)
	var team by teamProperty

	val technologiesProperty = SimpleObjectProperty<Map<String, Technology>>(technologies)
	var technologies by technologiesProperty

	val totalEconomyProperty = SimpleIntegerProperty(totalEconomy)
	var totalEconomy by totalEconomyProperty

	val totalIndustryProperty = SimpleIntegerProperty(totalIndustry)
	var totalIndustry by totalIndustryProperty

	val totalScienceProperty = SimpleIntegerProperty(totalScience)
	var totalScience by totalScienceProperty

	val totalShipsProperty = SimpleIntegerProperty(totalShips)
	var totalShips by totalShipsProperty

	val totalStarsProperty = SimpleIntegerProperty(totalStars)
	var totalStars by totalStarsProperty

	val economyTurn: Int
		get() = totalEconomy * 10 + technologies["banking"]!!.level * 75

	val industryTurn: Int
		get() = totalIndustry * (technologies["manufacturing"]!!.level + 5) / 24

	override fun compareTo(other: Player): Int {
		if (totalStars.compareTo(other.totalStars) != 0)
			return totalStars.compareTo(other.totalStars)
		if (totalShips.compareTo(other.totalShips) != 0)
			return totalShips.compareTo(other.totalShips)
		if ((totalEconomy + totalIndustry + totalScience).compareTo(other.totalEconomy + totalIndustry + totalScience) != 0)
			return (totalEconomy + totalIndustry + totalScience).compareTo(other.totalEconomy + totalIndustry + totalScience)
		if (technologies.values.sumBy { it.level }.compareTo(other.technologies.values.sumBy { it.level }) != 0)
			return technologies.values.sumBy { it.level }.compareTo(other.technologies.values.sumBy { it.level })
		return alias.compareTo(other.alias, ignoreCase = true)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Player) return false

		if (aiProperty != other.aiProperty) return false
		if (aliasProperty != other.aliasProperty) return false
		if (concededProperty != other.concededProperty) return false
		if (nameProperty != other.nameProperty) return false
		if (teamProperty != other.teamProperty) return false
		if (technologiesProperty != other.technologiesProperty) return false
		if (totalEconomyProperty != other.totalEconomyProperty) return false
		if (totalIndustryProperty != other.totalIndustryProperty) return false
		if (totalScienceProperty != other.totalScienceProperty) return false
		if (totalShipsProperty != other.totalShipsProperty) return false
		if (totalStarsProperty != other.totalStarsProperty) return false

		return true
	}

	override fun hashCode(): Int {
		var result = aiProperty.hashCode()
		result = 31 * result + aliasProperty.hashCode()
		result = 31 * result + concededProperty.hashCode()
		result = 31 * result + nameProperty.hashCode()
		result = 31 * result + teamProperty.hashCode()
		result = 31 * result + technologiesProperty.hashCode()
		result = 31 * result + totalEconomyProperty.hashCode()
		result = 31 * result + totalIndustryProperty.hashCode()
		result = 31 * result + totalScienceProperty.hashCode()
		result = 31 * result + totalShipsProperty.hashCode()
		result = 31 * result + totalStarsProperty.hashCode()
		return result
	}

	override fun toString(): String {
		return "Player(aiProperty=$aiProperty, aliasProperty=$aliasProperty, concededProperty=$concededProperty, nameProperty=$nameProperty, teamProperty=$teamProperty, technologiesProperty=$technologiesProperty, totalEconomyProperty=$totalEconomyProperty, totalIndustryProperty=$totalIndustryProperty, totalScienceProperty=$totalScienceProperty, totalShipsProperty=$totalShipsProperty, totalStarsProperty=$totalStarsProperty)"
	}
}