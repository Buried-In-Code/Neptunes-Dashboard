package macro.neptunes.backend

import macro.neptunes.config.Config.Companion.CONFIG
import macro.neptunes.Util.JsonToGame
import macro.neptunes.Util.JsonToPlayerMap
import macro.neptunes.game.GameTable
import macro.neptunes.game.GameTable.update
import macro.neptunes.player.PlayerTable
import macro.neptunes.player.PlayerTable.update
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2019-Feb-12.
 */
object Neptunes {
	private val LOGGER = LogManager.getLogger(Neptunes::class.java)
	private const val ENDPOINT = "http://nptriton.cqproject.net/game/"
	private val client = RESTClient(endpointUrl = ENDPOINT + CONFIG.gameID)

	internal fun updateGame() {
		val response = client.getRequest(endpoint = "/basic")
		if (response["Code"] == 200) {
			val data = (response["Data"] as String).JsonToGame()!!
			if (GameTable.select() == null)
				GameTable.insert(game = data)
			else {
				data.update(
					productions = data.productions,
					isGameOver = data.isGameOver,
					isPaused = data.isPaused,
					isStarted = data.isStarted,
					tick = data.tick
				)
			}
			updatePlayers()
		}
	}

	internal fun updatePlayers() {
		val response = client.getRequest(endpoint = "/players")
		if (response["Code"] == 200) {
			val data = (response["Data"] as String).JsonToPlayerMap()
			data.forEach { _, player ->
				if (player.alias.isBlank()) return@forEach
				if (PlayerTable.select(alias = player.alias) == null)
					PlayerTable.insert(player = player)
				else {
					val database = PlayerTable.select(alias = player.alias) ?: player
					var teamName = player.teamName
					if (database.teamName != "Free For All")
						teamName = database.teamName
					player.update(teamName = teamName, name = database.name)
				}
			}
		}
	}
}