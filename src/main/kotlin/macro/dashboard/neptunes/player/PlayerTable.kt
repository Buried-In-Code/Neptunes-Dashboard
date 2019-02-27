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
	).default("Free For All")
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
			uniqueIndex(nameCol, aliasCol)
			SchemaUtils.create(this)
		}
	}

	fun search(game: Game? = null): List<Player> = Util.query {
		var temp = game
		if (temp == null)
			temp = GameTable.search().firstOrNull() ?: throw GeneralException()
		select {
			gameCol eq temp.ID
		}.map {
			it.parse()
		}.sorted()
	}

	fun search(game: Game? = null, team: Team): List<Player> = Util.query {
		var temp = game
		if (temp == null)
			temp = GameTable.search().firstOrNull() ?: throw GeneralException()
		select {
			teamCol eq team.name and (gameCol eq temp.ID)
		}.map {
			it.parse()
		}.sorted()
	}

	fun select(game: Game? = null, alias: String): Player? = Util.query {
		var temp = game
		if (temp == null)
			temp = GameTable.search().firstOrNull() ?: throw GeneralException()
		select {
			aliasCol eq alias and (gameCol eq temp.ID)
		}.map {
			it.parse()
		}.sorted().firstOrNull()
	}

	fun insert(gameID: Long, update: PlayerUpdate) = Util.query {
		try {
			insert {
				it[gameCol] = EntityID(id = gameID, table = GameTable)
				it[aliasCol] = update.alias
				it[economyCol] = update.economy
				it[industryCol] = update.industry
				it[scienceCol] = update.science
				it[starsCol] = update.stars
				it[fleetCol] = update.fleet
				it[shipsCol] = update.ships
				it[isActiveCol] = update.isActive
			}
		} catch (esqle: ExposedSQLException) {
			LOGGER.error(esqle)
		}
	}

	fun update(player: Player): Player = Util.query {
		try {
			update({ aliasCol eq player.alias and (gameCol eq player.getGame().ID) }) {
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
			select(game = player.getGame(), alias = player.alias)!!
		} catch (esqle: ExposedSQLException) {
			select(game = player.getGame(), alias = player.alias)!!
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
		name: String? = this.name,
		economy: Int = this.economy,
		industry: Int = this.industry,
		science: Int = this.science,
		stars: Int = this.stars,
		fleet: Int = this.fleet,
		ships: Int = this.ships,
		isActive: Boolean = this.isActive
	): Player {
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