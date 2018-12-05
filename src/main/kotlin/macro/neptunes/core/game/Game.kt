package macro.neptunes.core.game

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Game(val name: String, val started: Boolean, val paused: Boolean, val totalStars: Int) {
	fun toJson(): Map<String, Any?> {
		val data = mapOf(
			"name" to name,
			"started" to started,
			"paused" to paused,
			"totalStars" to totalStars
		).toSortedMap()
		return data
	}
}