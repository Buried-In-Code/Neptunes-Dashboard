package macro.dashboard.neptunes.team

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import org.slf4j.LoggerFactory

/**
 * Last Updated by Macro303 on 2019-May-13
 */
object TeamHandler {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	private fun getTeamParam(ctx: Context): Int{
		return ctx.pathParam("team-id").toIntOrNull() ?: throw BadRequestResponse("Invalid Team ID")
	}

	internal fun getTeams(ctx: Context){
		val teams = TeamTable.search()
		ctx.json(teams.map { it.toMap() })
	}

	internal fun getTeam(ctx: Context){
		val teamID = getTeamParam(ctx)
		val team = TeamTable.select(ID = teamID) ?: throw NotFoundResponse("No Team with ID => $teamID")
		ctx.json(team.toMap())
	}

	internal fun addTeam(ctx: Context){
		ctx.json(mapOf("Result" to "Not Yet Implemented"))
		ctx.status(202)
	}

	internal fun updateTeam(ctx: Context){
		val teamID = getTeamParam(ctx)
		ctx.json(mapOf("Result" to "Not Yet Implemented"))
		ctx.status(202)
	}
}