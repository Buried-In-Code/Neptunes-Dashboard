package macro.neptunes.core.game

import macro.neptunes.core.Config.Companion.CONFIG
import macro.neptunes.core.Util.logger
import macro.neptunes.data.RESTClient
import kotlin.math.roundToInt
import kotlin.system.exitProcess

/**
 * Created by Macro303 on 2018-Nov-15.
 */
object GameHandler {
	private val LOGGER = logger()
	lateinit var game: Game

	private fun parse(data: Map<String, Any?>): Game? {
		val name = data.getOrDefault("name", null)?.toString()
			?: return null
		val started = data.getOrDefault("started", null)?.toString()?.toBoolean()
			?: return null
		val paused = data.getOrDefault("paused", null)?.toString()?.toBoolean()
			?: return null
		val totalStars = data.getOrDefault("total_stars", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		return Game(name = name, started = started, paused = paused, totalStars = totalStars)
	}

	@Suppress("UNCHECKED_CAST")
	fun refreshData() {
		LOGGER.info("Refreshing Game Data")
		val response = RESTClient.getRequest(endpoint = "/basic")
		val game = parse(data = response["Data"] as Map<String, Any?>)
		if (game == null) {
			LOGGER.error("Unable to find game with Game ID: {}", CONFIG.gameID)
			exitProcess(status = 0)
		}
		this.game = game
	}
}