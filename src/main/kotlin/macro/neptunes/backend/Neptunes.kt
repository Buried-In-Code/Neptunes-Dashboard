package macro.neptunes.backend

import macro.neptunes.Util
import macro.neptunes.Util.JsonToMap
import macro.neptunes.Util.JsonToPlayerMap
import macro.neptunes.config.Config.Companion.CONFIG
import macro.neptunes.game.GameTable
import macro.neptunes.game.GameTurnTable
import macro.neptunes.player.PlayerTable
import macro.neptunes.player.PlayerTable.update
import org.apache.logging.log4j.LogManager
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * Created by Macro303 on 2019-Feb-12.
 */
object Neptunes {
	private val LOGGER = LogManager.getLogger(Neptunes::class.java)
	private val client = RESTClient(endpointUrl = Util.ENDPOINT + CONFIG.gameID)

	internal fun updatePlayers() {
		val response = client.getRequest(endpoint = "/players")
		if (response["Code"] == 200) {
			val data = (response["Data"] as String).JsonToPlayerMap()
			data.forEach { _, player ->
				if (player?.alias?.isBlank() ?: return@forEach) return@forEach
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