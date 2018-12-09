package macro.neptunes.core.player

import macro.neptunes.core.Config.Companion.CONFIG
import macro.neptunes.data.RESTClient
import org.apache.logging.log4j.LogManager
import kotlin.math.roundToInt

/**
 * Created by Macro303 on 2018-Nov-15.
 */
object PlayerHandler {
	private val LOGGER = LogManager.getLogger(PlayerHandler::class.java)
	lateinit var players: List<Player>

	@Suppress("UNCHECKED_CAST")
	private fun parse(data: Map<String, Any?>): Player? {
		val alias: String = data["alias"] as String?
			?: return null
		val name: String = CONFIG.players.filter { it.key == alias }.values.firstOrNull()
			?: "Unknown"
		val industry: Int = (data["total_industry"] as Double?)?.roundToInt()
			?: return null
		val science: Int = (data["total_science"] as Double?)?.roundToInt()
			?: return null
		val economy: Int = (data["total_economy"] as Double?)?.roundToInt()
			?: return null
		val stars: Int = (data["total_stars"] as Double?)?.roundToInt()
			?: return null
		val fleet: Int = (data["total_fleets"] as Double?)?.roundToInt()
			?: return null
		val ships: Int = (data["total_strength"] as Double?)?.roundToInt()
			?: return null
		val isActive: Boolean = (data["conceded"] as Double?)?.roundToInt()?.equals(0)
			?: return null
		val banking: Int =
			(((data["tech"] as Map<String, Any?>?)?.get("banking") as Map<String, Any?>?)?.get("level") as Double?)?.roundToInt()
				?: return null
		val experimentation: Int =
			(((data["tech"] as Map<String, Any?>?)?.get("research") as Map<String, Any?>?)?.get("level") as Double?)?.roundToInt()
				?: return null
		val hyperspace: Int =
			(((data["tech"] as Map<String, Any?>?)?.get("propulsion") as Map<String, Any?>?)?.get("level") as Double?)?.roundToInt()
				?: return null
		val manufacturing: Int =
			(((data["tech"] as Map<String, Any?>?)?.get("manufacturing") as Map<String, Any?>?)?.get("level") as Double?)?.roundToInt()
				?: return null
		val scanning: Int =
			(((data["tech"] as Map<String, Any?>?)?.get("scanning") as Map<String, Any?>?)?.get("level") as Double?)?.roundToInt()
				?: return null
		val terraforming: Int =
			(((data["tech"] as Map<String, Any?>?)?.get("terraforming") as Map<String, Any?>?)?.get("level") as Double?)?.roundToInt()
				?: return null
		val weapons: Int =
			(((data["tech"] as Map<String, Any?>?)?.get("weapons") as Map<String, Any?>?)?.get("level") as Double?)?.roundToInt()
				?: return null
		return Player(
			name = name,
			alias = alias,
			industry = industry,
			science = science,
			economy = economy,
			stars = stars,
			fleet = fleet,
			ships = ships,
			isActive = isActive,
			banking = banking,
			experimentation = experimentation,
			hyperspace = hyperspace,
			manufacturing = manufacturing,
			scanning = scanning,
			terraforming = terraforming,
			weapons = weapons
		)
	}

	@Suppress("UNCHECKED_CAST")
	fun refreshData() {
		LOGGER.info("Refreshing Player Data")
		val players = ArrayList<Player>()
		val response = RESTClient.getRequest(endpoint = "/players")
		(response["Data"] as Map<String, Any?>).values.forEach {
			val player = parse(data = it as Map<String, Any?>) ?: return@forEach
			LOGGER.debug("Loaded Player: ${player.playerName()}")
			players.add(player)
		}
		this.players = players
	}

	fun sortByName(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ it.name },
			{ it.alias }
		))
	}

	fun sortByAlias(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ it.alias },
			{ it.name }
		))
	}

	fun sortByTeams(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ it.team },
			{ it.name },
			{ it.alias }
		))
	}

	fun sortByStars(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ -it.calcComplete() },
			{ -it.stars },
			{ it.name },
			{ it.alias }
		))
	}

	fun sortByShips(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ -it.ships },
			{ it.name },
			{ it.alias }
		))
	}

	fun sortByEconomy(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ -it.economy },
			{ it.name },
			{ it.alias }
		))
	}

	fun sortByIndustry(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ -it.industry },
			{ it.name },
			{ it.alias }
		))
	}

	fun sortByScience(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ -it.science },
			{ it.name },
			{ it.alias }
		))
	}

	fun filter(
		name: String = "",
		alias: String = "",
		team: String = "",
		players: List<Player> = this.players
	): List<Player> {
		return players
			.filter { it.name.contains(name, ignoreCase = true) }
			.filter { it.alias.contains(alias, ignoreCase = true) }
			.filter { it.team.contains(team, ignoreCase = true) }
	}
}