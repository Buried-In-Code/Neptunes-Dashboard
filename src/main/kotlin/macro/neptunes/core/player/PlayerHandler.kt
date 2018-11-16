package macro.neptunes.core.player

import macro.neptunes.core.Config
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
		val alias = data.getOrDefault("alias", null)?.toString()
			?: return null
		val name = Config.players.filter { it.key == alias }.values.firstOrNull()
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
		val banking = ((data.getOrDefault("tech", null) as Map<String, Any?>?)?.getOrDefault(
			"banking",
			null
		) as Map<String, Any?>?)?.getOrDefault("level", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val experimentation = ((data.getOrDefault("tech", null) as Map<String, Any?>?)?.getOrDefault(
			"research",
			null
		) as Map<String, Any?>?)?.getOrDefault("level", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val hyperspace = ((data.getOrDefault("tech", null) as Map<String, Any?>?)?.getOrDefault(
			"propulsion",
			null
		) as Map<String, Any?>?)?.getOrDefault("level", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val manufacturing = ((data.getOrDefault("tech", null) as Map<String, Any?>?)?.getOrDefault(
			"manufacturing",
			null
		) as Map<String, Any?>?)?.getOrDefault("level", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val scanning = ((data.getOrDefault("tech", null) as Map<String, Any?>?)?.getOrDefault(
			"scanning",
			null
		) as Map<String, Any?>?)?.getOrDefault("level", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val terraforming = ((data.getOrDefault("tech", null) as Map<String, Any?>?)?.getOrDefault(
			"terraforming",
			null
		) as Map<String, Any?>?)?.getOrDefault("level", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		val weapons = ((data.getOrDefault("tech", null) as Map<String, Any?>?)?.getOrDefault(
			"weapons",
			null
		) as Map<String, Any?>?)?.getOrDefault("level", null)?.toString()?.toDoubleOrNull()?.roundToInt()
			?: return null
		return Player(
			name = name,
			alias = alias,
			industry = industry,
			science = science,
			economy = economy,
			stars = stars,
			fleet = fleet,
			strength = strength,
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
		val players = ArrayList<Player>()
		val response = RESTClient.getRequest(endpoint = "/players")
		(response["Data"] as Map<String, Any?>).values.forEach {
			val player = parse(data = it as Map<String, Any?>)
			player ?: return@forEach
			LOGGER.info("Loaded Player: ${player.playerName()}")
			players.add(player)
		}
		this.players = players
	}

	fun sortByName(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy({ !it.isActive }, { it.name }, { it.alias }))
	}

	fun sortByAlias(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy({ !it.isActive }, { it.alias }, { it.name }))
	}

	fun sortByTeams(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy({ !it.isActive }, { it.team }, { it.name }, { it.alias }))
	}

	fun sortByStars(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(
			compareBy(
				{ !it.isActive },
				{ -it.calcComplete() },
				{ -it.stars },
				{ it.name },
				{ it.alias })
		)
	}

	fun sortByShips(players: List<Player> = this.players): List<Player> {
		return players.sortedWith(compareBy({ !it.isActive }, { -it.strength }, { it.name }, { it.alias }))
	}

	fun filter(
		name: String = "",
		alias: String = "",
		team: String = "",
		players: List<Player> = this.players
	): List<Player> {
		return players.filter { it.name.contains(name, ignoreCase = true) }
			.filter { it.alias.contains(alias, ignoreCase = true) }.filter { it.team.contains(team, ignoreCase = true) }
	}

	fun getTableData(players: List<Player> = this.players): List<Map<String, Any>> {
		val output: ArrayList<Map<String, Any>> = ArrayList()
		players.forEach {
			val playerData = linkedMapOf(
				Pair("Player", it.playerName()),
				Pair("Team", it.team),
				Pair("Stars", "${it.stars} (${it.calcComplete()}%)"),
				Pair("Ships", it.strength),
				Pair("Economy", it.economy),
				Pair("$/Turn", it.calcMoney()),
				Pair("Industry", it.industry),
				Pair("Ships/Turn", it.calcShips()),
				Pair("Science", it.science)
			)
			if (Config.enableTeams)
				playerData.remove("Team")
			output.add(playerData)
		}
		return output
	}
}