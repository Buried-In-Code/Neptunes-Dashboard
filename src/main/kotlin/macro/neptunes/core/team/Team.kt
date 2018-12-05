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

	fun shortJSON(): Map<String, Any> {
		val data = mapOf(
			"name" to name,
			"stars" to totalStars,
			"percentage" to calcComplete(),
			"members" to members.map { it.playerName() }.toSortedSet()
		)
		return data.toSortedMap()
	}

	fun longJSON(): Map<String, Any> {
		val data: Map<String, Any> = mapOf(
			"name" to name,
			"stars" to totalStars,
			"percentage" to calcComplete(),
			"members" to members.map { it.playerName() to it.stars }.toMap(),
			"ships" to totalStrength,
			"fleet" to totalFleet,
			"economy" to totalEconomy,
			"economy_turn" to calcMoney(),
			"industry" to totalIndustry,
			"industry_turn" to calcShips(),
			"science" to totalScience,
			"technology" to mapOf(
				"scanning" to scanning,
				"hyperspace" to hyperspace,
				"terraforming" to terraforming,
				"experimentation" to experimentation,
				"weapons" to weapons,
				"banking" to banking,
				"manufacturing" to manufacturing
			).toSortedMap()
		)
		return data.toSortedMap()
	}

	companion object {
		private val LOGGER = LoggerFactory.getLogger(Team::class.java)
	}
}