package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.GeneralException
import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.backend.PlayerUpdate
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.team.Team
import macro.dashboard.neptunes.team.TeamTable
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*

/**
 * Created by Macro303 on 2019-Feb-11.
 */
object PlayerTable : Table(name = "Player") {
	private val gameCol: Column<EntityID<Long>> = reference(
		name = "gameID",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	private val teamCol: Column<String> = reference(
		name = "teamName",
		refColumn = TeamTable.nameCol,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	).default(defaultValue = "Free For All")
	private val aliasCol: Column<String> = text(name = "alias")
	private val nameCol: Column<String?> = text(name = "name").nullable()
	private val economyCol: Column<Int> = integer(name = "economy")
	private val industryCol: Column<Int> = integer(name = "industry")
	private val scienceCol: Column<Int> = integer(name = "science")
	private val starsCol: Column<Int> = integer(name = "stars")
	private val fleetCol: Column<Int> = integer(name = "fleet")
	private val shipsCol: Column<Int> = integer(name = "ships")
	private val isActiveCol: Column<Boolean> = bool(name = "isActive")

	private val LOGGER = LogManager.getLogger(PlayerTable::class.java)

	init {
		Util.query {
			uniqueIndex(gameCol, aliasCol)
			SchemaUtils.create(this)
		}
	}

	fun search(gameID: Long? = null): List<Player> = Util.query {
		var temp = gameID
		if (temp == null)
			temp = GameTable.search().firstOrNull()?.ID ?: throw GeneralException()
		select {
			gameCol eq temp
		}.map {
			it.parse()
		}.sorted()
	}

	fun search(gameID: Long? = null, team: Team): List<Player> = Util.query {
		var temp = gameID
		if (temp == null)
			temp = GameTable.search().firstOrNull()?.ID ?: throw GeneralException()
		select {
			teamCol eq team.name and (gameCol eq temp)
		}.map {
			it.parse()
		}.sorted()
	}

	fun select(gameID: Long? = null, alias: String): Player? = Util.query {
		var temp = gameID
		if (temp == null)
			temp = GameTable.search().firstOrNull()?.ID ?: throw GeneralException()
		select {
			aliasCol eq alias and (gameCol eq temp)
		}.map {
			it.parse()
		}.sorted().firstOrNull()
	}

	fun insert(gameID: Long?, update: PlayerUpdate): Boolean = Util.query {
		var temp = gameID
		if (temp == null)
			temp = GameTable.search().firstOrNull()?.ID ?: throw GeneralException()
		try {
			insert {
				it[gameCol] = EntityID(id = temp, table = GameTable)
				it[aliasCol] = update.alias
				it[economyCol] = update.economy
				it[industryCol] = update.industry
				it[scienceCol] = update.science
				it[starsCol] = update.stars
				it[fleetCol] = update.fleet
				it[shipsCol] = update.ships
				it[isActiveCol] = update.isActive
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun update(gameID: Long?, alias: String, teamName: String?, name: String?): Player = Util.query {
		var temp = gameID
		if (temp == null)
			temp = GameTable.search().firstOrNull()?.ID ?: throw GeneralException()
		try {
			update({ aliasCol eq alias and (gameCol eq gameID) }) {
				it[teamCol] = teamName ?: "Free For All"
				it[nameCol] = name
			}
			select(gameID = temp, alias = alias)!!
		} catch (esqle: ExposedSQLException) {
			select(gameID = temp, alias = alias)!!
		}
	}

	fun update(gameID: Long?, update: PlayerUpdate): Boolean = Util.query {
		var temp = gameID
		if (temp == null)
			temp = GameTable.search().firstOrNull()?.ID ?: throw GeneralException()
		try {
			update({ aliasCol eq update.alias and (gameCol eq temp) }) {
				it[economyCol] = update.economy
				it[industryCol] = update.industry
				it[scienceCol] = update.science
				it[starsCol] = update.stars
				it[fleetCol] = update.fleet
				it[shipsCol] = update.ships
				it[isActiveCol] = update.isActive
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	private fun ResultRow.parse(): Player = Player(
		gameID = this[gameCol].value,
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
		name: String? = this.name
	): Player {
		this.teamName = teamName
		this.name = name
		return update(gameID = this.gameID, alias = this.alias, teamName = this.teamName, name = this.name)
	}
}