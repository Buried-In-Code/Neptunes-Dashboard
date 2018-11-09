package macro.neptunes.data

import macro.neptunes.core.Config
import macro.neptunes.core.game.Game
import macro.neptunes.core.player.Player
import kotlin.math.roundToInt

/**
 * Created by Macro303 on 2018-Nov-09.
 */
object Parser {

	internal fun parseGame(data: Map<String, Any?>): Game? {
		val name = data.getOrDefault("name", null)?.toString()
				?: return null
		val started = data.getOrDefault("started", null)?.toString()?.toBoolean()
				?: return null
		val paused = data.getOrDefault("paused", null)?.toString()?.toBoolean()
				?: return null
		val totalStars = data.getOrDefault("total_stars", null)?.toString()?.toDoubleOrNull()?.roundToInt()
				?: return null
		return Game(name = name, started = started, paused = paused, totalStars = totalStars)
	}

	@Suppress("UNCHECKED_CAST")
	internal fun parsePlayer(data: Map<String, Any?>): Player? {
		val alias = data.getOrDefault("alias", null)?.toString()
				?: return null
		val name = Config.players.filter { it.value == alias }.keys.firstOrNull()
				?: "Unknown"
		val industry = data.getOrDefault("total_industry", null)?.toString()?.toDoubleOrNull()?.roundToInt()
				?: return null
		val science = data.getOrDefault("total_science", null)?.toString()?.toDoubleOrNull()?.roundToInt()
				?: return null
		val economy = data.getOrDefault("total_economy", null)?.toString()?.toDoubleOrNull()?.roundToInt()
				?: return null
		val stars = data.getOrDefault("total_stars", null)?.toString()?.toDoubleOrNull()?.roundToInt()
				?: return null
		val fleet = data.getOrDefault("total_fleets", null)?.toString()?.toDoubleOrNull()?.roundToInt()
				?: return null
		val strength = data.getOrDefault("total_strength", null)?.toString()?.toDoubleOrNull()?.roundToInt()
				?: return null
		val isActive = data.getOrDefault("conceded", null)?.toString()?.toDoubleOrNull()?.roundToInt()?.equals(0)
				?: return null
		val banking = ((data.getOrDefault("tech", null) as Map<String, Any?>?)?.getOrDefault("banking", null) as Map<String, Any?>?)?.getOrDefault("level", null)?.toString()?.toDoubleOrNull()?.roundToInt()
				?: return null
		val experimentation = ((data.getOrDefault("tech", null) as Map<String, Any?>?)?.getOrDefault("research", null) as Map<String, Any?>?)?.getOrDefault("level", null)?.toString()?.toDoubleOrNull()?.roundToInt()
				?: return null
		val hyperspace = ((data.getOrDefault("tech", null) as Map<String, Any?>?)?.getOrDefault("propulsion", null) as Map<String, Any?>?)?.getOrDefault("level", null)?.toString()?.toDoubleOrNull()?.roundToInt()
				?: return null
		val manufacturing = ((data.getOrDefault("tech", null) as Map<String, Any?>?)?.getOrDefault("manufacturing", null) as Map<String, Any?>?)?.getOrDefault("level", null)?.toString()?.toDoubleOrNull()?.roundToInt()
				?: return null
		val scanning = ((data.getOrDefault("tech", null) as Map<String, Any?>?)?.getOrDefault("scanning", null) as Map<String, Any?>?)?.getOrDefault("level", null)?.toString()?.toDoubleOrNull()?.roundToInt()
				?: return null
		val terraforming = ((data.getOrDefault("tech", null) as Map<String, Any?>?)?.getOrDefault("terraforming", null) as Map<String, Any?>?)?.getOrDefault("level", null)?.toString()?.toDoubleOrNull()?.roundToInt()
				?: return null
		val weapons = ((data.getOrDefault("tech", null) as Map<String, Any?>?)?.getOrDefault("weapons", null) as Map<String, Any?>?)?.getOrDefault("level", null)?.toString()?.toDoubleOrNull()?.roundToInt()
				?: return null
		return Player(name = name, alias = alias, industry = industry, science = science, economy = economy, stars = stars, fleet = fleet, strength = strength, isActive = isActive, banking = banking, experimentation = experimentation, hyperspace = hyperspace, manufacturing = manufacturing, scanning = scanning, terraforming = terraforming, weapons = weapons)
	}
}