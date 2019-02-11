package macro.neptunes.core

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Team(
	val ID: Int,
	val game: Game,
	var name: String
) : Comparable<Team> {

	override fun compareTo(other: Team): Int {
		return byGame.then(byName).compare(this, other)
	}

	fun toOutput(): Map<String, Any> = mapOf(
		"ID" to ID,
		"name" to name
	).toSortedMap()

	companion object {
		internal val byGame = compareBy(Team::game)
		internal val byName = compareBy(String.CASE_INSENSITIVE_ORDER, Team::name)
	}
}
/*
data class Team(val name: String) : Comparable<Team> {
	val members = ArrayList<Player>()
	val totalIndustry: Int
		get() = members.stream().mapToInt { it.industry }.sum()
	val totalScience: Int
		get() = members.stream().mapToInt { it.science }.sum()
	val totalEconomy: Int
		get() = members.stream().mapToInt { it.economy }.sum()
	val totalStars: Int
		get() = members.stream().mapToInt { it.stars }.sum()
	val totalFleet: Int
		get() = members.stream().mapToInt { it.fleet }.sum()
	val totalShips: Int
		get() = members.stream().mapToInt { it.ships }.sum()
	val isActive: Boolean
		get() = members.stream().anyMatch { it.isActive }
	val banking: Int
		get() = members.stream().mapToInt { it.banking }.sum().div(members.size)
	val experimentation: Int
		get() = members.stream().mapToInt { it.experimentation }.sum().div(members.size)
	val hyperspace: Int
		get() = members.stream().mapToInt { it.hyperspace }.sum().div(members.size)
	val manufacturing: Int
		get() = members.stream().mapToInt { it.manufacturing }.sum().div(members.size)
	val scanning: Int
		get() = members.stream().mapToInt { it.scanning }.sum().div(members.size)
	val terraforming: Int
		get() = members.stream().mapToInt { it.terraforming }.sum().div(members.size)
	val weapons: Int
		get() = members.stream().mapToInt { it.weapons }.sum().div(members.size)

	fun calcComplete(): Double {
		val total = GameHandler.game.totalStars.toDouble()
		return totalStars.div(total)
	}

	fun hasWon(): Boolean {
		return calcComplete() > 50.0
	}

	fun calcMoney(): Int {
		return totalEconomy * 10 + banking * 75
	}

	fun calcShips(): Int {
		return totalIndustry * (manufacturing + 5) / 24
	}

	override fun compareTo(other: Team): Int {
		return byStars.then(byName).compare(this, other)
	}

	fun toJson(): Map<String, Any> {
		val data = mapOf(
			"name" to name,
			"totalStars" to totalStars,
			"totalShips" to totalShips,
			"totalFleet" to totalFleet,
			"totalEconomy" to totalEconomy,
			"totalIndustry" to totalIndustry,
			"totalScience" to totalScience,
			"percent" to calcComplete(),
			"economyTurn" to calcMoney(),
			"industryTurn" to calcShips(),
			"members" to members.map { it.toJson() },
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
		private val LOGGER = LogManager.getLogger(Team::class.java)
		internal val byName = compareBy(String.CASE_INSENSITIVE_ORDER, Team::name)
		internal val byStars = compareBy(Team::totalStars)
		internal val byShips = compareBy(Team::totalShips)
		internal val byEconomy = compareBy(Team::totalEconomy)
		internal val byIndustry = compareBy(Team::totalIndustry)
		internal val byScience = compareBy(Team::totalScience)
	}
}*/
