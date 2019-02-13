package macro.neptunes.player

import macro.neptunes.team.Team
import macro.neptunes.team.TeamTable

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
	var isActive: Boolean
): Comparable<Player>{

	fun getTeam(): Team = TeamTable.select(name = teamName)!!

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
			"isActive" to isActive
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
/*
data class player(
	val name: String,
	val alias: String,
	var team: String = "Unknown",
	val industry: Int,
	val science: Int,
	val economy: Int,
	val stars: Int,
	val fleet: Int,
	val ships: Int,
	val isActive: Boolean,
	val scanning: Int,
	val hyperspace: Int,
	val terraforming: Int,
	val experimentation: Int,
	val weapons: Int,
	val banking: Int,
	val manufacturing: Int
) : Comparable<player> {
	fun playerName() = "$name ($alias)"

	fun calcComplete(): Double {
		val total = GameHandler.game.totalStars.toDouble()
		return stars.div(total)
	}

	fun hasWon(): Boolean {
		return calcComplete() > 50.0
	}

	fun calcMoney(): Int {
		return economy * 10 + banking * 75
	}

	fun calcShips(): Int {
		return industry * (manufacturing + 5) / 24
	}

	override fun compareTo(other: player): Int {
		return byTeam.then(byName).then(byAlias).compare(this, other)
	}

	fun toJson(): Map<String, Any?> {
		val data = mapOf(
			"name" to name,
			"alias" to alias,
			"team" to team,
			"industry" to industry,
			"science" to science,
			"economy" to economy,
			"stars" to stars,
			"fleet" to fleet,
			"ships" to ships,
			"isActive" to isActive,
			"team" to team,
			"percent" to calcComplete(),
			"economyTurn" to calcMoney(),
			"industryTurn" to calcShips(),
			"technology" to mapOf(
				"scanning" to scanning,
				"hyperspace" to hyperspace,
				"terraforming" to terraforming,
				"experimentation" to experimentation,
				"weapons" to weapons,
				"banking" to banking,
				"manufacturing" to manufacturing
			).toSortedMap()
		).toSortedMap()
		return data
	}

	companion object {
		private val LOGGER = LogManager.getLogger(player::class.java)
		internal val byName = compareBy(String.CASE_INSENSITIVE_ORDER, player::name)
		internal val byAlias = compareBy(String.CASE_INSENSITIVE_ORDER, player::alias)
		internal val byTeam = compareBy(String.CASE_INSENSITIVE_ORDER, player::team)
		internal val byStars = compareBy(player::stars)
		internal val byShips = compareBy(player::ships)
		internal val byEconomy = compareBy(player::economy)
		internal val byIndustry = compareBy(player::industry)
		internal val byScience = compareBy(player::science)
	}
}*/
