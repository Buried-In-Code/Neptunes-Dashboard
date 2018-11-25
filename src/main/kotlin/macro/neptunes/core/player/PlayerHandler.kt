package macro.neptunes.core.player

import macro.neptunes.core.Config.Companion.CONFIG
import macro.neptunes.core.Util.logger
import macro.neptunes.data.RESTClient
import kotlin.math.roundToInt

/**
 * Created by Macro303 on 2018-Nov-15.
 */
object PlayerHandler {
	private val LOGGER = logger()
	lateinit var players: List<Player>

	@Suppress("UNCHECKED_CAST")
	private fun parse(data: Map<String, Any?>): Player? {
		val alias: String = data["alias"] as String?
			?: return null
		LOGGER.debug("Alias: $alias")
		val name: String = CONFIG.players.filter { it.key == alias }.values.firstOrNull()
			?: "Unknown"
		LOGGER.debug("Name: $name")
		val industry: Int = (data["total_industry"] as Double?)?.roundToInt()
			?: return null
		LOGGER.debug("Industry: $industry")
		val science: Int = (data["total_science"] as Double?)?.roundToInt()
			?: return null
		LOGGER.debug("Science: $science")
		val economy: Int = (data["total_economy"] as Double?)?.roundToInt()
			?: return null
		LOGGER.debug("Economy: $economy")
		val stars: Int = (data["total_stars"] as Double?)?.roundToInt()
			?: return null
		LOGGER.debug("Stars: $stars")
		val fleet: Int = (data["total_fleets"] as Double?)?.roundToInt()
			?: return null
		LOGGER.debug("Fleet: $fleet")
		val ships: Int = (data["total_strength"] as Double?)?.roundToInt()
			?: return null
		LOGGER.debug("Ships: $ships")
		val isActive: Boolean = (data["conceded"] as Double?)?.roundToInt()?.equals(0)
			?: return null
		LOGGER.debug("Is Active: $isActive")
		val banking: Int =
			(((data["tech"] as Map<String, Any?>?)?.get("banking") as Map<String, Any?>?)?.get("level") as Double?)?.roundToInt()
				?: return null
		LOGGER.debug("Banking: $banking")
		val experimentation: Int =
			(((data["tech"] as Map<String, Any?>?)?.get("research") as Map<String, Any?>?)?.get("level") as Double?)?.roundToInt()
				?: return null
		LOGGER.debug("Experimentation: $experimentation")
		val hyperspace: Int =
			(((data["tech"] as Map<String, Any?>?)?.get("propulsion") as Map<String, Any?>?)?.get("level") as Double?)?.roundToInt()
				?: return null
		LOGGER.debug("Hyperspace: $hyperspace")
		val manufacturing: Int =
			(((data["tech"] as Map<String, Any?>?)?.get("manufacturing") as Map<String, Any?>?)?.get("level") as Double?)?.roundToInt()
				?: return null
		LOGGER.debug("Manufacturing: $manufacturing")
		val scanning: Int =
			(((data["tech"] as Map<String, Any?>?)?.get("scanning") as Map<String, Any?>?)?.get("level") as Double?)?.roundToInt()
				?: return null
		LOGGER.debug("Scanning: $scanning")
		val terraforming: Int =
			(((data["tech"] as Map<String, Any?>?)?.get("terraforming") as Map<String, Any?>?)?.get("level") as Double?)?.roundToInt()
				?: return null
		LOGGER.debug("Terraforming: $terraforming")
		val weapons: Int =
			(((data["tech"] as Map<String, Any?>?)?.get("weapons") as Map<String, Any?>?)?.get("level") as Double?)?.roundToInt()
				?: return null
		LOGGER.debug("Weapons: $weapons")
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
			{ !it.isActive },
			{ it.name },
			{ it.alias }
		))
	}

	fun sortByAlias(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ !it.isActive },
			{ it.alias },
			{ it.name }
		))
	}

	fun sortByTeams(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ !it.isActive },
			{ it.team },
			{ it.name },
			{ it.alias }
		))
	}

	fun sortByStars(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ !it.isActive },
			{ -it.calcComplete() },
			{ -it.stars },
			{ it.name },
			{ it.alias }
		))
	}

	fun sortByShips(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ !it.isActive },
			{ -it.ships },
			{ it.name },
			{ it.alias }
		))
	}

	fun sortByEconomy(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ !it.isActive },
			{ -it.economy },
			{ it.name },
			{ it.alias }
		))
	}

	fun sortByIndustry(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ !it.isActive },
			{ -it.industry },
			{ it.name },
			{ it.alias }
		))
	}

	fun sortByScience(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy(
			{ !it.isActive },
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

	fun getTableData(players: List<Player> = this.players): List<Map<String, Any>> {
		val output: List<Map<String, Any>> = players.map { it.longJSON() }
		return output
	}
}