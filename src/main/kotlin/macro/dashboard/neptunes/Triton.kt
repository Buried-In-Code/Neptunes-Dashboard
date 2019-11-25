package macro.dashboard.neptunes

import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import macro.dashboard.neptunes.config.Config
import macro.dashboard.neptunes.cycle.CycleTable
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.PlayerTable
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2019-Nov-25
 */
object Triton {
	private val LOGGER = LogManager.getLogger(this::class.java)
	private val URL = "https://np.ironhelmet.com/api"

	@Suppress("UNCHECKED_CAST")
	fun getGame(gameID: Long, code: String) {
		val request = Util.postRequest(url = URL, gameID = gameID, code = code) ?: return
		val response = Util.postRequest(url = "https://np.ironhelmet.com/api", gameID = gameID, code = code)
		response ?: return
		LOGGER.debug("Response: $response")
		try {
			val gameObject = Util.GSON.fromJson<ProteusData>(response, ProteusData::class.java).scanningData
			LOGGER.debug("Game: $gameObject")
			val valid = GameTable.insert(ID = gameID, update = gameObject)
			if (!valid)
				GameTable.update(update = gameObject)
			gameObject.players.values.filter { it.alias.isNotBlank() }.forEach {
				PlayerTable.insert(gameID = gameID, update = it)
				PlayerTable.search(alias = it.alias).firstOrNull()?.apply {
					CycleTable.insert(playerID = this.ID, cycle = gameObject.tick / Config.CONFIG.game.cycle, update = it)
				} ?: throw NotFoundException(message = "Unable to Find Player => ${it.alias}")
			}
		}catch (jse: JsonSyntaxException){
			throw GeneralException(message = "Invalid response from Backend")
		}
	}
}