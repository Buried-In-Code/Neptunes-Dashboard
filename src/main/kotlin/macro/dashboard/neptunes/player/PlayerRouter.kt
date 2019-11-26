package macro.dashboard.neptunes.player

import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.team.TeamTable
import org.apache.logging.log4j.LogManager
import java.util.*

/**
 * Last Updated by Macro303 on 2019-May-10
 */
object PlayerRouter {
	private val LOGGER = LogManager.getLogger(PlayerRouter::class.java)

	fun getPlayers(game: Game): List<Player> {
		return PlayerTable.search(gameId = game.ID)
	}

	@KtorExperimentalAPI
	fun getPlayer(game: Game, alias: String): Player {
		return PlayerTable.select(gameId = game.ID, alias = alias) ?: throw NotFoundException()
	}

	@KtorExperimentalAPI
	fun updatePlayer(game: Game, alias: String, update: PlayerUpdate?): Player {
		val player = PlayerTable.select(gameId = game.ID, alias = alias)
			?: throw NotFoundException()
		player.name = update?.name ?: player.name
		player.teamId = TeamTable.select(UUID.fromString(update?.team ?: player.teamId.toString()))?.uuid
			?: TeamTable.select(gameId = game.ID, name = "Free For All")?.uuid
					?: throw NotFoundException()
		return player.update()
	}

	data class PlayerUpdate(val name: String?, val team: String?)
}