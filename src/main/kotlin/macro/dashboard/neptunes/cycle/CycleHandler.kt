package macro.dashboard.neptunes.cycle

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import org.slf4j.LoggerFactory

/**
 * Last Updated by Macro303 on 2019-May-10
 */
object CycleHandler {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	private fun getPlayerParam(ctx: Context): Int {
		return ctx.pathParam("player-id").toIntOrNull() ?: throw BadRequestResponse("Invalid Player ID")
	}

	private fun getCycleParam(ctx: Context): Int {
		return ctx.pathParam("cycle").toIntOrNull() ?: throw BadRequestResponse("Invalid Cycle Number")
	}

	internal fun getCycles(ctx: Context){
		val playerID = getPlayerParam(ctx = ctx)
		val cycles = CycleTable.searchByPlayer(playerID = playerID)
		ctx.json(cycles)
	}

	internal fun getCycle(ctx: Context){
		val playerID = getPlayerParam(ctx = ctx)
		val cycleNum = getCycleParam(ctx = ctx)
		val cycle = CycleTable.select(playerID = playerID, cycle = cycleNum) ?: throw NotFoundResponse("No Cycle Found with Player ID => $playerID at Cycle => $cycleNum")
		ctx.json(cycle)
	}
}