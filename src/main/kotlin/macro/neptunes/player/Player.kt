package macro.neptunes.player

import macro.neptunes.team.Team
import macro.neptunes.team.TeamTable
import macro.neptunes.technology.PlayerTechnology
import macro.neptunes.technology.Technology

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Player(
	var teamName: String,
	val alias: String,
	var name: String? = null,
	var economy: Int,
	var industry: Int,
	var science: Int,
	var stars: Int,
	var fleet: Int,
	var ships: Int,
	var isActive: Boolean,
	var technologies: PlayerTechnology
): Comparable<Player>{

	fun getTeam(): Team = TeamTable.select(name = teamName)!!
	fun getEconomyTurn(): Double = economy * 10 + 1.0 * 75
	fun getIndustryTurn(): Double = industry * (1.0 + 5) / 24

	override fun compareTo(other: Player): Int {
		return byTeam.then(byAlias).compare(this, other)
	}

	fun toOutput(showParent: Boolean = false, showChildren: Boolean = true): Map<String, Any?> {
		var output = mapOf(
			"alias" to alias,
			"name" to name,
			"economy" to economy,
			"industry" to industry,
			"science" to science,
			"stars" to stars,
			"fleet" to fleet,
			"ships" to ships,
			"isActive" to isActive,
			"economyTurn" to getEconomyTurn(),
			"industryTurn" to getIndustryTurn()
		)
		output = when (showParent) {
			true -> output.plus("team" to getTeam().toOutput(showChildren = false))
			false -> output.plus("team" to teamName)
		}
		return output.toSortedMap()
	}

	companion object {
		internal val byTeam = compareBy(Player::getTeam)
		internal val byAlias = compareBy(String.CASE_INSENSITIVE_ORDER, Player::alias)
	}
}
