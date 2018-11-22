package macro.neptunes.core.player

import macro.neptunes.core.config.Config
import macro.neptunes.core.Util
import macro.neptunes.core.game.GameHandler
import org.slf4j.LoggerFactory
import java.text.NumberFormat

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
	private val LOGGER = LoggerFactory.getLogger(Player::class.java)
	var team: String = "Unknown"

	fun playerName() = "$name ($alias)"

	fun calcComplete(): Double {
		val total = GameHandler.game.totalStars.toDouble()
		return stars.div(total)
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
			"Name" to name,
			"Alias" to alias,
			"Team" to team,
			"Star Percentage" to calcComplete()
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
				is Int -> temp = Util.INT_FORMAT.format(value)
				is Double -> temp = Util.PERCENT_FORMAT.format(value)
			}
			output += "<li><b>$key:</b> $temp</li>"
		}
		output += "</ul>"
		return output
	}

	fun longJSON(): Map<String, Any> {
		val data = mapOf(
			"Name" to name,
			"Alias" to alias,
			"Team" to team,
			"Star Percentage" to calcComplete(),
			"Stars" to stars,
			"Fleet" to fleet,
			"Industry" to industry,
			"Science" to science,
			"Economy" to economy,
			"Ships" to strength,
			"Technology" to mapOf(
				"Scanning" to scanning,
				"Hyperspace" to hyperspace,
				"Terraforming" to terraforming,
				"Experimentation" to experimentation,
				"Weapons" to weapons,
				"Banking" to banking,
				"Manufacturing" to manufacturing
			)
		)
		return if (!Config.enableTeams) data.filterNot { it.key == "Team" } else data
	}
}