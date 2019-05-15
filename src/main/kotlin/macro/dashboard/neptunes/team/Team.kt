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
	val players by lazy {
		PlayerTable.search(team = name)
	}

	fun toMap(showPlayers: Boolean = true, showLatestCycle: Boolean = false): Map<String, Any?> {
		return mapOf(
			"ID" to ID,
			"name" to name,
			"players" to (if (showPlayers) players.map { it.toMap(showLatestCycle = showLatestCycle) } else players.map { it.ID }),
			"cycles" to mapOf(
				"cycle" to players.maxBy { it.latestCycle?.cycle ?: 0 },
				"stars" to players.sumBy { it.latestCycle?.stars ?: 0 },
				"ships" to players.sumBy { it.latestCycle?.ships ?: 0 },
				"economy" to players.sumBy { it.latestCycle?.economy ?: 0 },
				"economyPerCycle" to players.sumBy { it.latestCycle?.economyPerCycle ?: 0 },
				"industry" to players.sumBy { it.latestCycle?.industry ?: 0 },
				"industryPerCycle" to players.sumBy { it.latestCycle?.industryPerCycle ?: 0 },
				"science" to players.sumBy { it.latestCycle?.science ?: 0 },
				"sciencePerCycle" to players.sumBy { it.latestCycle?.sciencePerCycle ?: 0 },
				"tech" to mapOf(
					"scanning" to players.sumBy { it.latestCycle?.scanning ?: 0 },
					"range" to players.sumBy { it.latestCycle?.range ?: 0 },
					"experimentation" to players.sumBy { it.latestCycle?.experimentation ?: 0 },
					"weapons" to players.sumBy { it.latestCycle?.weapons ?: 0 },
					"banking" to players.sumBy { it.latestCycle?.banking ?: 0 },
					"manufacturing" to players.sumBy { it.latestCycle?.manufacturing ?: 0 }
				).toSortedMap()
			).toSortedMap()
		).toSortedMap()
	}

	companion object {
		private val LOGGER = LoggerFactory.getLogger(this::class.java)
	}
}