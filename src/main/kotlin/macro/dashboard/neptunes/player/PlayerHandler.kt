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

	private fun getTeamQuery(ctx: Context): String{
		return ctx.queryParam("team") ?: "%"
	}

	private fun getNameQuery(ctx: Context): String{
		return ctx.queryParam("name") ?: "%"
	}

	private fun getAliasQuery(ctx: Context): String{
		return ctx.queryParam("alias") ?: "%"
	}

	internal fun getPlayers(ctx: Context) {
		val team = getTeamQuery(ctx = ctx)
		val name = getNameQuery(ctx = ctx)
		val alias = getAliasQuery(ctx = ctx)
		val players = PlayerTable.search(team = team, name = name, alias = alias)
		ctx.json(players.map { it.toMap() })
	}

	internal fun getPlayer(ctx: Context) {
		val playerID = getPlayerParam(ctx)
		val player = PlayerTable.select(ID = playerID) ?: throw NotFoundResponse("No Player with ID => $playerID")
		ctx.json(player.toMap())
	}

	internal fun updatePlayer(ctx: Context) {
		val playerID = getPlayerParam(ctx)
		ctx.json(mapOf("Result" to "Not Yet Implemented"))
		ctx.status(202)
	}
}