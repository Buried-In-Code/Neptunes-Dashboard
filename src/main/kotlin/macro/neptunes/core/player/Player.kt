package macro.neptunes.core.player

import macro.neptunes.core.Config
import macro.neptunes.core.game.GameHandler
import kotlin.math.roundToInt

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Player(
	val name: String,
	val alias: String,
	val industry: Int,
	val science: Int,
	val economy: Int,
	val stars: Int,
	val fleet: Int,
	val strength: Int,
	val isActive: Boolean,
	val scanning: Int,
	val hyperspace: Int,
	val terraforming: Int,
	val experimentation: Int,
	val weapons: Int,
	val banking: Int,
	val manufacturing: Int
) {
	var team: String = "Unknown"

	fun playerName() = "$alias ($name)"

	fun calcComplete(): Int {
		val total = GameHandler.game.totalStars.toDouble()
		return (stars.div(total).times(10000)).roundToInt().div(100.0).roundToInt()
	}

	fun hasWon(): Boolean {
		return calcComplete() > Config.winPercentage
	}

	fun calcMoney(): Int {
		return economy * 10 + banking * 75
	}

	fun calcShips(): Int {
		return industry * (manufacturing + 5) / 24
	}

	fun shortHTML(): String {
		return shortJSON().values.joinToString(" = ")
	}

	fun shortJSON(): Map<String, Any> {
		val data = mapOf(
			Pair("Name", playerName()),
			Pair("Team", team),
			Pair("Win Percentage", calcComplete())
		)
		return if (!Config.enableTeams) data.filterNot { it.key == "Team" } else data
	}

	fun longHTML(): String {
		var output = "<b>$name</b><ul>"
		longJSON().forEach {key, value ->
			output += "<li><b>$key:</b> $value</li>"
		}
		output += "</ul>"
		return output
	}

	fun longJSON(): Map<String, Any> {
		val data = mapOf(
			Pair("Name", name),
			Pair("Alias", alias),
			Pair("Team", team),
			Pair("Stars", stars),
			Pair("Win Percentage", calcComplete()),
			Pair("Fleet", fleet),
			Pair("Industry", industry),
			Pair("Science", science),
			Pair("Economy", economy),
			Pair("Ships", strength)
		)
		return if (!Config.enableTeams) data.filterNot { it.key == "Team" } else data
	}
}