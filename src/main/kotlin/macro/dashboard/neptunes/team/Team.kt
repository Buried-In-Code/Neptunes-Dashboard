package macro.dashboard.neptunes.team

import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.Player
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
	val game: Game by lazy {
		GameTable.select()
	}
	val players: List<Player> by lazy {
		PlayerTable.searchByTeam(teamID = ID)
	}

	val totalEconomy by lazy {
		players.sumBy { it.latestCycle.economy }
	}
	val totalEconomyPerCycle by lazy {
		players.sumBy { it.latestCycle.economyPerCycle }
	}
	val totalIndustry by lazy {
		players.sumBy { it.latestCycle.industry }
	}
	val totalIndustryPerCycle by lazy {
		players.sumBy { it.latestCycle.industryPerCycle }
	}
	val totalScience by lazy {
		players.sumBy { it.latestCycle.science }
	}
	val totalSciencePerCycle by lazy {
		players.sumBy { it.latestCycle.sciencePerCycle }
	}
	val totalStars by lazy {
		players.sumBy { it.latestCycle.stars }
	}
	val totalFleet by lazy {
		players.sumBy { it.latestCycle.fleet }
	}
	val totalShips by lazy {
		players.sumBy { it.latestCycle.ships }
	}

	fun toOutput(showGame: Boolean, showPlayers: Boolean): Map<String, Any?> {
		val output = mapOf<String, Any?>(
			"ID" to ID,
			"name" to name,
			"game" to gameID,
			"players" to players.map { it.ID },
			"totalEconomy" to totalEconomy,
			"totalEconomyPerCycle" to totalEconomyPerCycle,
			"totalIndustry" to totalIndustry,
			"totalIndustryPerCycle" to totalIndustryPerCycle,
			"totalScience" to totalScience,
			"totalSciencePerCycle" to totalSciencePerCycle,
			"totalStars" to totalStars,
			"totalFleet" to totalFleet,
			"totalShips" to totalShips
		).toMutableMap()
		if (showGame)
			output["game"] = game.toOutput()
		if (showPlayers)
			output["players"] = players.map { it.toOutput(showGame = false, showTeam = false) }
		return output.toSortedMap()
	}

	companion object {
		private val LOGGER = LoggerFactory.getLogger(this::class.java)
	}
}