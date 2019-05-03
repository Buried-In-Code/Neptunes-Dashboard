package macro.dashboard.neptunes.team

import macro.dashboard.neptunes.GeneralException
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.Player
import macro.dashboard.neptunes.player.PlayerTable
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Team(
	val ID: Int,
	val gameID: Long,
	var name: String
) {

	val game: Game by lazy {
		GameTable.select(ID = gameID) ?: throw GeneralException()
	}
	val players: List<Player> by lazy {
		PlayerTable.searchByTeam(teamID = ID)
	}

	val totalEconomy: Int by lazy {
		players.sumBy { it.latestTurn.economy }
	}
	val totalEconomyPerTurn: Double by lazy {
		players.sumByDouble { it.latestTurn.economyPerTurn }
	}
	val totalIndustry: Int by lazy {
		players.sumBy { it.latestTurn.industry }
	}
	val totalIndustryPerTurn: Double by lazy {
		players.sumByDouble { it.latestTurn.industryPerTurn }
	}
	val totalScience: Int by lazy {
		players.sumBy { it.latestTurn.science }
	}
	val totalStars: Int by lazy {
		players.sumBy { it.latestTurn.stars }
	}
	val totalFleet: Int by lazy {
		players.sumBy { it.latestTurn.fleet }
	}
	val totalShips: Int by lazy {
		players.sumBy { it.latestTurn.ships }
	}

	fun toOutput(showGame: Boolean, showPlayers: Boolean): Map<String, Any?> {
		val output = mapOf<String, Any?>(
			"ID" to ID,
			"name" to name,
			"game" to gameID,
			"players" to players.map { it.ID },
			"totalEconomy" to totalEconomy,
			"totalEconomyPerTurn" to totalEconomyPerTurn,
			"totalIndustry" to totalIndustry,
			"totalIndustryPerTurn" to totalIndustryPerTurn,
			"totalScience" to totalScience,
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
		private val LOGGER = LogManager.getLogger()
	}
}