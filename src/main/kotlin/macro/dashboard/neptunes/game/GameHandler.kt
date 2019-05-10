package macro.dashboard.neptunes.game

import io.javalin.http.Context
import macro.dashboard.neptunes.Config
import macro.dashboard.neptunes.backend.Proteus
import org.slf4j.LoggerFactory

/**
 * Last Updated by Macro303 on 2019-May-10
 */
object GameHandler {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	internal fun getGame(ctx: Context){
		val game = GameTable.select()
		ctx.json(game)
	}

	internal fun updateGame(ctx: Context){
		val successful = Proteus.getGame(gameID = Config.CONFIG.gameID, code = Config.CONFIG.gameCode) ?: false
		ctx.status(if (successful) 204 else 500)
	}
}