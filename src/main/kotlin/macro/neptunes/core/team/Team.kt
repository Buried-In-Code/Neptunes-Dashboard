package macro.neptunes.core.team

import macro.neptunes.core.Config.Companion.CONFIG
import macro.neptunes.core.game.GameHandler
import macro.neptunes.core.player.Player
import org.slf4j.LoggerFactory
import java.text.NumberFormat

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Team(val name: String) {
	val members = ArrayList<Player>()
	val totalIndustry: Int
		get() = members.stream().mapToInt { it.industry }.sum()
	val totalScience: Int
		get() = members.stream().mapToInt { it.science }.sum()
	val totalEconomy: Int
		get() = members.stream().mapToInt { it.economy }.sum()
	val totalStars: Int
		get() = members.stream().mapToInt { it.stars }.sum()
	val totalFleet: Int
		get() = members.stream().mapToInt { it.fleet }.sum()
	val totalStrength: Int
		get() = members.stream().mapToInt { it.ships }.sum()
	val isActive: Boolean
		get() = members.stream().anyMatch { it.isActive }
	val banking: Int
		get() = members.stream().mapToInt { it.banking }.sum().div(members.size)
	val experimentation: Int
		get() = members.stream().mapToInt { it.experimentation }.sum().div(members.size)
	val hyperspace: Int
		get() = members.stream().mapToInt { it.hyperspace }.sum().div(members.size)
	val manufacturing: Int
		get() = members.stream().mapToInt { it.manufacturing }.sum().div(members.size)
	val scanning: Int
		get() = members.stream().mapToInt { it.scanning }.sum().div(members.size)
	val terraforming: Int
		get() = members.stream().mapToInt { it.terraforming }.sum().div(members.size)
	val weapons: Int
		get() = members.stream().mapToInt { it.weapons }.sum().div(members.size)

	fun calcComplete(): Double {
		val total = GameHandler.game.totalStars.toDouble()
		return totalStars.div(total)
	}

	fun hasWon(): Boolean {
		return calcComplete() > CONFIG.starPercentage
	}

	fun calcMoney(): Int {
		return totalEconomy * 10 + banking * 75
	}

	fun calcShips(): Int {
		return totalIndustry * (manufacturing + 5) / 24
	}

	fun shortHTML(): String {
		var output = "<b>$name</b><ul>"
		shortJSON().filterNot { it.key == "Name" }.forEach { key, value ->
			var temp = value.toString()
			if (key == "Star Percentage")
				temp += "%"
			when (value) {
				is List<*> -> {
					temp = "<ul>"
					value.forEach {
						temp += "<li>$it</li>"
					}
					temp += "</ul>"
				}
				is Int -> {
					temp = NumberFormat.getIntegerInstance().format(value)
				}
			}
			output += "<li><b>$key:</b> $temp</li>"
		}
		output += "</ul>"
		return output
	}

	fun shortJSON(): Map<String, Any> {
		val data = mapOf(
			"Name" to name,
			"Star Percentage" to calcComplete(),
			"Members" to members.map { it.playerName() }.toSortedSet()
		)
		return data.toSortedMap()
	}

	fun longHTML(): String {
		var output = "<b>$name</b><ul>"
		longJSON().filterNot { it.key == "Name" }.forEach { key, value ->
			var temp = value.toString()
			if (key == "Star Percentage")
				temp += "%"
			when (value) {
				is List<*> -> {
					temp = "<ul>"
					value.forEach {
						temp += "<li>$it</li>"
					}
					temp += "</ul>"
				}
				is Int -> {
					temp = NumberFormat.getIntegerInstance().format(value)
				}
			}
			output += "<li><b>$key:</b> $temp</li>"
		}
		output += "</ul>"
		return output
	}

	fun longJSON(): Map<String, Any> {
		val data: Map<String, Any> = mapOf(
			"Name" to name,
			"Star Percentage" to calcComplete(),
			"Members" to members.map { it.playerName() }.toSortedSet(),
			"Stars" to totalStars,
			"Fleet" to totalFleet,
			"Industry" to totalIndustry,
			"Science" to totalScience,
			"Economy" to totalEconomy,
			"Ships" to totalStrength,
			"Technology" to mapOf(
				"Scanning" to scanning,
				"Hyperspace" to hyperspace,
				"Terraforming" to terraforming,
				"Experimentation" to experimentation,
				"Weapons" to weapons,
				"Banking" to banking,
				"Manufacturing" to manufacturing
			).toSortedMap()
		)
		return data.toSortedMap()
	}

	companion object {
		private val LOGGER = LoggerFactory.getLogger(Team::class.java)
	}
}