package macro.dashboard.neptunes.team

import macro.dashboard.neptunes.player.PlayerTable
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Team(
	val ID: Int,
	val gameID: Long,
	var name: String
) {
	fun getPlayers() = PlayerTable.searchByTeam(teamID = ID)

	fun toMap(): Map<String, Any?> {
		return mapOf(
			"ID" to ID,
			"name" to name,
			"players" to getPlayers().map { it.toMap() }
		).toSortedMap()
	}

	companion object {
		private val LOGGER = LoggerFactory.getLogger(this::class.java)
	}
}