package macro.dashboard.neptunes.tick

import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import macro.dashboard.neptunes.game.Game
import org.apache.logging.log4j.LogManager

/**
 * Last Updated by Macro303 on 2019-May-10
 */
object TickRouter {
	private val LOGGER = LogManager.getLogger(TickRouter::class.java)

	fun getTicks(game: Game, alias: String): List<Tick> {
		return TickTable.search(gameId = game.ID, playerId = alias)
	}

	@KtorExperimentalAPI
	fun getTick(game: Game, alias: String, tick: Int): Tick {
		return TickTable.select(gameId = game.ID, playerId = alias, tick = tick) ?: throw NotFoundException()
	}
}