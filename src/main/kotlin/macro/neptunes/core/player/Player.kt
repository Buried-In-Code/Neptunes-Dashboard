package macro.neptunes.core.player

import macro.neptunes.core.Config.Companion.CONFIG
import macro.neptunes.core.Util
import macro.neptunes.core.game.GameHandler
import org.slf4j.LoggerFactory
import java.text.NumberFormat

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
) {
	private val LOGGER = LoggerFactory.getLogger(Player::class.java)
	var team: String = "Unknown"

	fun playerName() = "$name ($alias)"

	fun calcComplete(): Double {
		val total = GameHandler.game.totalStars.toDouble()
		return stars.div(total)
	}

	fun hasWon(): Boolean {
		return calcComplete() > CONFIG.starPercentage
	}

	fun calcMoney(): Int {
		return economy * 10 + banking * 75
	}

	fun calcShips(): Int {
		return industry * (manufacturing + 5) / 24
	}

	fun shortJSON(): Map<String, Any> {
		val data = mapOf(
			"name" to name,
			"alias" to alias,
			"team" to team,
			"stars" to stars,
			"percentage" to calcComplete()
		)
		return (if (!CONFIG.enableTeams) data.filterNot { it.key == "team" } else data).toSortedMap()
	}

	fun longJSON(): Map<String, Any> {
		val data = mapOf(
			"name" to name,
			"alias" to alias,
			"team" to team,
			"stars" to stars,
			"percentage" to calcComplete(),
			"fleet" to fleet,
			"industry" to industry,
			"science" to science,
			"economy" to economy,
			"ships" to ships,
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
		return (if (!CONFIG.enableTeams) data.filterNot { it.key == "team" } else data).toSortedMap()
	}

	companion object {
		private val LOGGER = LoggerFactory.getLogger(Player::class.java)
	}
}