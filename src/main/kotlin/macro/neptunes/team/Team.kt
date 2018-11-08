package macro.neptunes.team

import macro.neptunes.game.Game
import macro.neptunes.player.Player
import kotlin.math.roundToInt

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
		get() = members.stream().mapToInt { it.strength }.sum()

	fun calculateComplete(game: Game): Double {
		val total = game.totalStars.toDouble()
		return (totalStars.div(total).times(10000)).roundToInt().div(100.0)
	}
}