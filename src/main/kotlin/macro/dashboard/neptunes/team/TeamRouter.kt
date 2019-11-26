package macro.dashboard.neptunes.team

import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import macro.dashboard.neptunes.game.Game
import org.apache.logging.log4j.LogManager

/**
 * Last Updated by Macro303 on 2019-May-13
 */
object TeamRouter {
	private val LOGGER = LogManager.getLogger(TeamRouter::class.java)

	fun getTeams(game: Game): List<Team> {
		return TeamTable.search(gameId = game.ID)
	}

	@KtorExperimentalAPI
	fun getTeam(game: Game, name: String): Team {
		return TeamTable.select(gameId = game.ID, name = name) ?: throw NotFoundException()
	}

	@KtorExperimentalAPI
	fun updateTeam(game: Game, name: String, update: TeamUpdate?): Team {
		val team = TeamTable.select(gameId = game.ID, name = name) ?: throw NotFoundException()
		team.name = update?.name ?: team.name
		return team.update()
	}

	data class TeamUpdate(val name: String?)
}