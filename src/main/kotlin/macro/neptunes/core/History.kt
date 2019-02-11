package macro.neptunes.core

import macro.neptunes.data.GameTable

/**
 * Created by Macro303 on 2019-Feb-08.
 */
data class History(val gameID: Long, var teamName: String?, var winnerNames: List<String>) {
	val game: Game? by lazy {
		GameTable.select(ID = gameID)
	}
}