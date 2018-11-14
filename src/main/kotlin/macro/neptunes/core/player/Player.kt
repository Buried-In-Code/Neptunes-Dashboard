package macro.neptunes.core.player

import macro.neptunes.core.Config
import macro.neptunes.core.game.Game
import kotlin.math.roundToInt

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Player(val name: String, val alias: String, val industry: Int, val science: Int, val economy: Int, val stars: Int, val fleet: Int, val strength: Int, val isActive: Boolean, val scanning: Int, val hyperspace: Int, val terraforming: Int, val experimentation: Int, val weapons: Int, val banking: Int, val manufacturing: Int) {
	var team: String = "Unknown"

	fun playerName() = "$alias ($name)"

	fun calcComplete(game: Game): Double {
		val total = game.totalStars.toDouble()
		return (stars.div(total).times(10000)).roundToInt().div(100.0)
	}

	fun hasWon(game: Game): Boolean {
		return calcComplete(game = game) > Config.winPercentage
	}

	fun calcMoney(): Int {
		return economy * 10 + banking * 75
	}

	fun calcShips(): Int {
		return industry * (manufacturing + 5) / 24
	}
}