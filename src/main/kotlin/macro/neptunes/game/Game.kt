package macro.neptunes.game

import macro.neptunes.team.Team
import macro.neptunes.Util
import macro.neptunes.team.TeamTable
import java.time.LocalDateTime

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Game(
	val ID: Long,
	val name: String,
	val totalStars: Int,
	val victoryStars: Int,
	val admin: Int,
	val fleetSpeed: Double,
	val isTurnBased: Boolean,
	val productionRate: Int,
	val tickRate: Int,
	val tradeCost: Int,
	val turnBasedTimeout: Int
) : Comparable<Game> {
	fun getTeams(): List<Team> = TeamTable.search()

	override fun compareTo(other: Game): Int {
		return byName.compare(this, other)
	}

	companion object {
		internal val byName = compareBy(String.CASE_INSENSITIVE_ORDER, Game::name)
	}
}