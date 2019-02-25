package macro.neptunes.player

import macro.neptunes.Util
import macro.neptunes.team.Team
import macro.neptunes.team.TeamTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*

/**
 * Created by Macro303 on 2019-Feb-11.
 */
object PlayerTable : Table(name = "Player") {
	private val teamCol: Column<String> = reference(
		name = "teamName",
		refColumn = TeamTable.nameCol,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	).default("Free For All")
	val aliasCol: Column<String> = text(name = "alias")
	private val nameCol: Column<String?> = text(name = "name").nullable()
	private val economyCol: Column<Int> = integer(name = "economy").default(0)
	private val industryCol: Column<Int> = integer(name = "industry").default(0)
	private val scienceCol: Column<Int> = integer(name = "science").default(0)
	private val starsCol: Column<Int> = integer(name = "stars").default(0)
	private val fleetCol: Column<Int> = integer(name = "fleet").default(0)
	private val shipsCol: Column<Int> = integer(name = "ships").default(0)
	private val isActiveCol: Column<Boolean> = bool(name = "isActive").default(false)

	init {
		Util.query {
			uniqueIndex(nameCol, aliasCol)
			SchemaUtils.create(this)
		}
	}

	fun search(): List<Player> = Util.query {
		selectAll().map {
			it.parse()
		}
	}

	fun search(team: Team): List<Player> = Util.query {
		select {
			teamCol eq team.name
		}.map {
			it.parse()
		}
	}

	fun select(alias: String): Player? = Util.query {
		select {
			aliasCol eq alias
		}.map {
			it.parse()
		}.firstOrNull()
	}

	fun insert(
		team: Team,
		alias: String,
		name: String? = null,
		economy: Int,
		industry: Int,
		science: Int,
		stars: Int,
		fleet: Int,
		ships: Int,
		isActive: Boolean
	): Boolean = Util.query {
		try {
			insert {
				it[teamCol] = team.name
				it[aliasCol] = alias
				it[nameCol] = name
				it[economyCol] = economy
				it[industryCol] = industry
				it[scienceCol] = science
				it[starsCol] = stars
				it[fleetCol] = fleet
				it[shipsCol] = ships
				it[isActiveCol] = isActive
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun insert(player: Player): Boolean = Util.query {
		insert(
			team = player.getTeam(),
			alias = player.alias,
			name = player.name,
			economy = player.economy,
			industry = player.industry,
			science = player.science,
			stars = player.stars,
			fleet = player.fleet,
			ships = player.ships,
			isActive = player.isActive
		)
	}

	fun update(player: Player): Boolean = Util.query {
		try {
			update({ aliasCol eq player.alias }) {
				it[teamCol] = player.teamName
				it[nameCol] = player.name
				it[economyCol] = player.economy
				it[industryCol] = player.industry
				it[scienceCol] = player.science
				it[starsCol] = player.stars
				it[fleetCol] = player.fleet
				it[shipsCol] = player.ships
				it[isActiveCol] = player.isActive
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	private fun ResultRow.parse(): Player = Player(
		teamName = this[teamCol],
		name = this[nameCol],
		alias = this[aliasCol],
		economy = this[economyCol],
		industry = this[industryCol],
		science = this[scienceCol],
		stars = this[starsCol],
		fleet = this[fleetCol],
		ships = this[shipsCol],
		isActive = this[isActiveCol]
	)

	fun Player.update(
		teamName: String = this.teamName,
		name: String? = this.name,
		economy: Int = this.economy,
		industry: Int = this.industry,
		science: Int = this.science,
		stars: Int = this.stars,
		fleet: Int = this.fleet,
		ships: Int = this.ships,
		isActive: Boolean = this.isActive
	): Boolean {
		this.teamName = teamName
		this.name = name
		this.economy = economy
		this.industry = industry
		this.science = science
		this.stars = stars
		this.fleet = fleet
		this.ships = ships
		this.isActive = isActive
		return update(player = this)
	}
}