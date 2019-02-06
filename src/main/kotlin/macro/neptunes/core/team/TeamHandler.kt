package macro.neptunes.core.team

import macro.neptunes.core.Config.Companion.CONFIG
import macro.neptunes.core.player.PlayerHandler
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-15.
 */
object TeamHandler {
	private val LOGGER = LogManager.getLogger(TeamHandler::class.java)
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
}