package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.cycle.CycleTable
import macro.dashboard.neptunes.team.TeamTable
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Player(
	val ID: Int,
	val gameID: Long,
	var teamID: Int,
	val alias: String,
	var name: String? = null
) {
	val team by lazy {
		TeamTable.select(ID = teamID)
	}
	val cycles by lazy {
		CycleTable.searchByPlayer(playerID = ID)
	}
	val latestCycle by lazy {
		CycleTable.selectLatest(playerID = ID)
	}

	fun toMap(showLatestCycle: Boolean = false): Map<String, Any?> {
		return mapOf(
			"ID" to ID,
			"name" to name,
			"alias" to alias,
			"team" to (team?.name ?: ""),
			"cycles" to (if (showLatestCycle) latestCycle?.toMap()
				?: emptyMap<String, Any?>() else cycles.map { it.toMap() })
		).toSortedMap()
	}

	companion object {
		private val LOGGER = LoggerFactory.getLogger(this::class.java)
	}
}