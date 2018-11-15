package macro.neptunes.core.team

import macro.neptunes.core.Config
import macro.neptunes.core.player.Player
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-15.
 */
object TeamHandler {
	private val LOGGER = LogManager.getLogger(TeamHandler::class.java)

	fun getData(players: List<Player>): List<Team> {
		val teams = ArrayList<Team>()
		Config.teams.forEach { key, value ->
			val team = Team(name = key)
			players.forEach { player ->
				if (value.contains(player.name)) {
					player.team = key
					team.members.add(player)
				}
			}
			LOGGER.info("Loaded Team: ${team.name}")
			teams.add(team)
		}
		return teams
	}
}