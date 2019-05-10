package macro.dashboard.neptunes.player

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import org.slf4j.LoggerFactory

/**
 * Last Updated by Macro303 on 2019-May-10
 */
object PlayerHandler {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	private fun getPlayerParam(ctx: Context): Int {
		return ctx.pathParam("player-id").toIntOrNull() ?: throw BadRequestResponse("Invalid Player ID")
	}

	internal fun getPlayers(ctx: Context) {
		val player = PlayerTable.search()
		ctx.json(player)
	}

	internal fun getPlayer(ctx: Context) {
		val playerID = getPlayerParam(ctx)
		val player = PlayerTable.select(ID = playerID) ?: throw NotFoundResponse("No Player with ID => $playerID")
		ctx.json(player)
	}

	internal fun updatePlayer(ctx: Context) {
		val playerID = getPlayerParam(ctx)
		val successful = false
		throw NotFoundResponse("Not Yet Implemented")
		ctx.status(if (successful) 204 else 500)
	}
}