package macro.neptunes.core.team

import macro.neptunes.core.Config
import macro.neptunes.core.player.PlayerHandler
import org.apache.logging.log4j.LogManager
import java.text.NumberFormat

/**
 * Created by Macro303 on 2018-Nov-15.
 */
object TeamHandler {
	private val LOGGER = LogManager.getLogger(TeamHandler::class.java)
	var teams: List<Team>? = null

	fun refreshData() {
		val teams = ArrayList<Team>()
		Config.teams.forEach { key, value ->
			val team = Team(name = key)
			PlayerHandler.players.forEach { player ->
				if (value.contains(player.name)) {
					player.team = key
					team.members.add(player)
				}
			}
			LOGGER.info("Loaded Team: ${team.name}")
			teams.add(team)
		}
		this.teams = teams
	}

	fun sortByName(teams: List<Team>? = this.teams): List<Team>? {
		return teams?.sortedWith(compareBy({ !it.isActive }, { it.name }))
	}

	fun sortByStars(teams: List<Team>? = this.teams): List<Team>? {
		return teams?.sortedWith(compareBy({ !it.isActive }, { -it.calcComplete() }, { -it.totalStars }, { it.name }))
	}

	fun sortByShips(teams: List<Team>? = this.teams): List<Team>? {
		return teams?.sortedWith(compareBy({ !it.isActive }, { -it.totalStrength }, { it.name }))
	}

	fun filter(
		name: String = "",
		playerName: String = "",
		playerAlias: String = "",
		teams: List<Team>? = this.teams
	): List<Team>? {
		return teams?.filter { it.name.contains(name, ignoreCase = true) }
			?.filter { it.members.find { it.name.contains(playerName, ignoreCase = true) } != null }
			?.filter { it.members.find { it.alias.contains(playerAlias, ignoreCase = true) } != null }
	}

	fun getTableData(teams: List<Team>? = this.teams): List<Map<String, Any>> {
		val output: ArrayList<Map<String, Any>> = ArrayList()
		teams?.forEach {
			val teamData = linkedMapOf(
				Pair("Name", it.name),
				Pair("Stars", "${it.totalStars} (${it.calcComplete()}%)"),
				Pair("Ships", it.totalStrength),
				Pair("Economy", it.totalEconomy),
				Pair("$/Turn", it.calcMoney()),
				Pair("Industry", it.totalIndustry),
				Pair("Ships/Turn", it.calcShips()),
				Pair("Science", it.totalScience)
			)
			teamData.forEach { key, value ->
				if (value is Int) teamData[key] = NumberFormat.getIntegerInstance().format(value)
			}
			output.add(teamData)
		}
		return output
	}
}