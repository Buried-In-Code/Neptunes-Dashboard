package macro.neptunes.player

import macro.neptunes.game.Game
import kotlin.math.roundToInt

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Player(val alias: String, val industry: Int, val science: Int, val economy: Int, val stars: Int, val fleet: Int, val strength: Int) {

	fun calculateComplete(game: Game): Double {
		val total = game.totalStars.toDouble()
		return (stars.div(total).times(10000)).roundToInt().div(100.0)
	}

	companion object {
		fun parse(data: Map<String, Any?>): Player? {
			val alias = data.getOrDefault("alias", null)?.toString() ?: return null
			val industry = data.getOrDefault("total_industry", null)?.toString()?.toDoubleOrNull()?.roundToInt()
					?: return null
			val science = data.getOrDefault("total_science", null)?.toString()?.toDoubleOrNull()?.roundToInt()
					?: return null
			val economy = data.getOrDefault("total_economy", null)?.toString()?.toDoubleOrNull()?.roundToInt()
					?: return null
			val stars = data.getOrDefault("total_stars", null)?.toString()?.toDoubleOrNull()?.roundToInt()
					?: return null
			val fleet = data.getOrDefault("total_fleets", null)?.toString()?.toDoubleOrNull()?.roundToInt()
					?: return null
			val strength = data.getOrDefault("total_strength", null)?.toString()?.toDoubleOrNull()?.roundToInt()
					?: return null
			return Player(alias = alias, industry = industry, science = science, economy = economy, stars = stars, fleet = fleet, strength = strength)
		}
	}
}