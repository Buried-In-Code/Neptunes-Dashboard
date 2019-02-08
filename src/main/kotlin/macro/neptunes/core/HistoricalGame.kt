package macro.neptunes.core

import macro.neptunes.core.game.Game
import macro.neptunes.core.game.GameHandler

/**
 * Created by Macro303 on 2019-Feb-08.
 */
data class HistoricalGame(val gameID: Long, var teamName: String?, var winnerNames: List<String>) {
	val game: Game? by lazy {
		GameHandler.getGame(gameID = gameID)
	}
}