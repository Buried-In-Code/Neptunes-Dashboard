package macro.neptunes.game

import kotlin.math.roundToInt

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Game(val name: String, val started: Boolean, val paused: Boolean, val totalStars: Int) {

	companion object {
		fun parse(data: Map<String, Any?>): Game? {
			val name = data.getOrDefault("name", null)?.toString() ?: return null
			val started = data.getOrDefault("started", null)?.toString()?.toBoolean() ?: return null
			val paused = data.getOrDefault("paused", null)?.toString()?.toBoolean() ?: return null
			val totalStars = data.getOrDefault("total_stars", null)?.toString()?.toDoubleOrNull()?.roundToInt()
					?: return null
			return Game(name = name, started = started, paused = paused, totalStars = totalStars)
		}
	}
}