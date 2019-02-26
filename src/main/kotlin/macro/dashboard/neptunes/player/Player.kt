package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.GeneralException
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.team.Team
import macro.dashboard.neptunes.team.TeamTable

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Player(
	val gameID: Long,
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
) : Comparable<Player> {

	/*private fun getTechnologies(): List<Technology> = TechnologyTable.search(player = this)
	fun getScanning(): Technology = getTechnologies().first { it.name == "Scanning" }
	fun getHyperspace(): Technology = getTechnologies().first { it.name == "Hyperspace" }
	fun getTerraforming(): Technology = getTechnologies().first { it.name == "Terraforming" }
	fun getExperimentation(): Technology = getTechnologies().first { it.name == "Experimentation" }
	fun getWeapons(): Technology = getTechnologies().first { it.name == "Weapons" }
	fun getBanking(): Technology = getTechnologies().first { it.name == "Banking" }
	fun getManufacturing(): Technology = getTechnologies().first { it.name == "Manufacturing" }*/

	fun getGame(): Game = GameTable.select(ID = gameID) ?: GameTable.search().firstOrNull() ?: throw GeneralException()
	fun getTeam(): Team = TeamTable.select(name = teamName) ?: TeamTable.insert(game = getGame(), name = teamName)
	fun getEconomyTurn(): Double = /*economy * 10 + getBanking().value * 75*/ 42.0
	fun getIndustryTurn(): Double = /*industry * (getManufacturing().value + 5) / 24*/ 42.0

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
			"industryTurn" to getIndustryTurn()/*,
			"technologies" to getTechnologies()*/
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
