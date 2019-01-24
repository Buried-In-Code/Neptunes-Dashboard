package macro.neptunes.core.player

import macro.neptunes.core.game.GameHandler
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Player(
	val name: String,
	val alias: String,
	val industry: Int,
	val science: Int,
	val economy: Int,
	val stars: Int,
	val fleet: Int,
	val ships: Int,
	val isActive: Boolean,
	val scanning: Int,
	val hyperspace: Int,
	val terraforming: Int,
	val experimentation: Int,
	val weapons: Int,
	val banking: Int,
	val manufacturing: Int
) : Comparable<Player> {
	var team: String = "Unknown"

	fun playerName() = "$name ($alias)"

	fun calcComplete(): Double {
		val total = GameHandler.game.totalStars.toDouble()
		return stars.div(total)
	}

	fun hasWon(): Boolean {
		return calcComplete() > 50.0
	}

	fun calcMoney(): Int {
		return economy * 10 + banking * 75
	}

	fun calcShips(): Int {
		return industry * (manufacturing + 5) / 24
	}

	override fun compareTo(other: Player): Int {
		return byStars.then(byName).then(byAlias).compare(this, other)
	}

	fun toJson(): Map<String, Any?> {
		val data = mapOf(
			"name" to name,
			"alias" to alias,
			"industry" to industry,
			"science" to science,
			"economy" to economy,
			"stars" to stars,
			"fleet" to fleet,
			"ships" to ships,
			"isActive" to isActive,
			"team" to team,
			"percent" to calcComplete(),
			"economyTurn" to calcMoney(),
			"industryTurn" to calcShips(),
			"technology" to mapOf(
				"scanning" to scanning,
				"hyperspace" to hyperspace,
				"terraforming" to terraforming,
				"experimentation" to experimentation,
				"weapons" to weapons,
				"banking" to banking,
				"manufacturing" to manufacturing
			).toSortedMap()
		).toSortedMap()
		return data
	}

	companion object {
		private val LOGGER = LogManager.getLogger(Player::class.java)
		internal val byName = compareBy(String.CASE_INSENSITIVE_ORDER, Player::name)
		internal val byAlias = compareBy(String.CASE_INSENSITIVE_ORDER, Player::alias)
		internal val byTeam = compareBy(String.CASE_INSENSITIVE_ORDER, Player::team)
		internal val byStars = compareBy(Player::stars)
		internal val byShips = compareBy(Player::ships)
		internal val byEconomy = compareBy(Player::economy)
		internal val byIndustry = compareBy(Player::industry)
		internal val byScience = compareBy(Player::science)
	}
}