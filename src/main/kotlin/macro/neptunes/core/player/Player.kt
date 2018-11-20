package macro.neptunes.core.player

import macro.neptunes.core.Config
import macro.neptunes.core.game.GameHandler
import org.apache.logging.log4j.LogManager
import java.text.NumberFormat
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
	private val LOGGER = LogManager.getLogger(Player::class.java)
	var team: String = "Unknown"

	fun playerName() = "$name ($alias)"

	fun calcComplete(): Int {
		val total = GameHandler.game.totalStars.toDouble()
		return (stars.div(total).times(10000)).roundToInt().div(100.0).roundToInt()
	}

	fun hasWon(): Boolean {
		return calcComplete() > Config.starPercentage
	}

	fun calcMoney(): Int {
		return economy * 10 + banking * 75
	}

	fun calcShips(): Int {
		return industry * (manufacturing + 5) / 24
	}

	fun shortHTML(): String {
		var output = "<b>${playerName()}</b>"
		output += "<ul>"
		shortJSON().filterNot { it.key == "Name" || it.key == "Alias" }.forEach { key, value ->
			var temp = value.toString()
			if (key == "Star Percentage")
				temp += "%"
			when (value) {
				is Map<*, *> -> {
					temp = "<ul>"
					value.forEach {
						temp += "<li><b>${it.key}:</b> ${it.value}</li>"
					}
					temp += "</ul>"
				}
				is Int -> {
					temp = NumberFormat.getIntegerInstance().format(value)
				}
			}
			output += "<li><b>$key:</b> $temp</li>"
		}
		output += "</ul>"
		return output
	}

	fun shortJSON(): Map<String, Any> {
		val data = mapOf(
			Pair("Name", name),
			Pair("Alias", alias),
			Pair("Team", team),
			Pair("Star Percentage", calcComplete())
		)
		return if (!Config.enableTeams) data.filterNot { it.key == "Team" } else data
	}

	@Suppress("UNCHECKED_CAST")
	fun longHTML(): String {
		var output = "<b>${playerName()}</b>"
		output += "<ul>"
		longJSON().filterNot { it.key == "Name" || it.key == "Alias" }.forEach { key, value ->
			var temp = value.toString()
			if (key == "Star Percentage")
				temp += "%"
			when (value) {
				is Map<*, *> -> {
					temp = "<ul>"
					value.forEach {
						temp += "<li><b>${it.key}:</b> ${it.value}</li>"
					}
					temp += "</ul>"
				}
				is Int -> {
					temp = NumberFormat.getIntegerInstance().format(value)
				}
			}
			output += "<li><b>$key:</b> $temp</li>"
		}
		output += "</ul>"
		return output
	}

	fun longJSON(): Map<String, Any> {
		val data = mapOf(
			Pair("Name", name),
			Pair("Alias", alias),
			Pair("Team", team),
			Pair("Star Percentage", calcComplete()),
			Pair("Stars", stars),
			Pair("Fleet", fleet),
			Pair("Industry", industry),
			Pair("Science", science),
			Pair("Economy", economy),
			Pair("Ships", strength),
			Pair(
				"Technology", mapOf(
					Pair("Scanning", scanning),
					Pair("Hyperspace", hyperspace),
					Pair("Terraforming", terraforming),
					Pair("Experimentation", experimentation),
					Pair("Weapons", weapons),
					Pair("Banking", banking),
					Pair("Manufacturing", manufacturing)
				)
			)
		)
		return if (!Config.enableTeams) data.filterNot { it.key == "Team" } else data
	}
}