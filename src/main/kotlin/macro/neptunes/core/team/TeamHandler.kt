package macro.neptunes.core.team

import macro.neptunes.core.Config.Companion.CONFIG
import macro.neptunes.core.Util.logger
import macro.neptunes.core.player.PlayerHandler

/**
 * Created by Macro303 on 2018-Nov-15.
 */
object TeamHandler {
	private val LOGGER = logger()
	var teams: List<Team> = emptyList()

	fun refreshData() {
		LOGGER.info("Refreshing Team Data")
		val teams = ArrayList<Team>()
		CONFIG.teams.forEach { key, value ->
			val team = Team(name = key)
			PlayerHandler.players.filter { value.contains(it.name) }.forEach {
				it.team = key
				team.members.add(it)
			}
			LOGGER.debug("Loaded Team: ${team.name}")
			teams.add(team)
		}
		this.teams = teams
	}

	fun sortByName(teams: List<Team> = this.teams): List<Team> {
		return teams.sortedWith(compareBy(
			{ !it.isActive },
			{ it.name }
		))
	}

	fun sortByStars(teams: List<Team> = this.teams): List<Team> {
		return teams.sortedWith(compareBy(
			{ !it.isActive },
			{ -it.calcComplete() },
			{ -it.totalStars },
			{ it.name }
		))
	}

	fun sortByShips(teams: List<Team> = this.teams): List<Team> {
		return teams.sortedWith(compareBy(
			{ !it.isActive },
			{ -it.totalStrength },
			{ it.name }
		))
	}

	fun sortByEconomy(teams: List<Team> = this.teams): List<Team> {
		return teams.sortedWith(compareBy(
			{ !it.isActive },
			{ -it.totalEconomy },
			{ it.name }
		))
	}

	fun sortByIndustry(teams: List<Team> = this.teams): List<Team> {
		return teams.sortedWith(compareBy(
			{ !it.isActive },
			{ -it.totalIndustry },
			{ it.name }
		))
	}

	fun sortByScience(teams: List<Team> = this.teams): List<Team> {
		return teams.sortedWith(compareBy(
			{ !it.isActive },
			{ -it.totalScience },
			{ it.name }
		))
	}

	fun filter(
		name: String = "",
		playerName: String = "",
		playerAlias: String = "",
		teams: List<Team> = this.teams
	): List<Team> {
		return teams
			.filter { it.name.contains(name, ignoreCase = true) }
			.filter { it.members.find { it.name.contains(playerName, ignoreCase = true) } != null }
			.filter { it.members.find { it.alias.contains(playerAlias, ignoreCase = true) } != null }
	}

	fun getTableData(teams: List<Team> = this.teams): List<Map<String, Any>> {
		val output: List<Map<String, Any>> = teams.map { it.longJSON() }
		return output
	}
}