package macro.neptunes.data

import macro.neptunes.core.Game
import macro.neptunes.core.Player
import macro.neptunes.core.Team
import macro.neptunes.core.Util
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import kotlin.math.roundToInt

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
	private val teamCol: Column<EntityID<Int>?> = reference(
		name = "teamID",
		foreign = TeamTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	).nullable()
	private val aliasCol: Column<String> = text(name = "alias")
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

	fun search(alias: String): List<Player> = Util.query {
		select {
			aliasCol eq alias
		}.map {
			it.parse()
		}
	}

	fun select(game: Game, alias: String): Player? = Util.query {
		select {
			gameCol eq game.ID and (aliasCol eq alias)
		}.map {
			it.parse()
		}.firstOrNull()
	}

	fun insert(player: Player): Player = Util.query {
		insert {
			it[gameCol] = EntityID(player.game.ID, GameTable)
			it[teamCol] = EntityID(player.team?.ID, TeamTable)
			it[aliasCol] = player.alias
			it[nameCol] = player.name
			it[economyCol] = player.economy
			it[industryCol] = player.industry
			it[scienceCol] = player.science
			it[starsCol] = player.stars
			it[fleetCol] = player.fleet
			it[shipsCol] = player.ships
			it[isActiveCol] = player.isActive
		}
		select(game = player.game, alias = player.alias)!!
	}

	fun update(player: Player): Player = Util.query {
		update({ gameCol eq player.game.ID and(aliasCol eq aliasCol)}){
			it[teamCol] = EntityID(player.team?.ID, TeamTable)
			it[nameCol] = player.name
			it[economyCol] = player.economy
			it[industryCol] = player.industry
			it[scienceCol] = player.science
			it[starsCol] = player.stars
			it[fleetCol] = player.fleet
			it[shipsCol] = player.ships
			it[isActiveCol] = player.isActive
		}
		select(game = player.game, alias = player.alias)!!
	}

	fun insert(game: Game, data: Map<String, Any>): Player = Util.query {
		val player = select(game = game, alias = data["alias"] as String)
		if (player == null) {
			insert {
				it[gameCol] = EntityID(game.ID, GameTable)
				it[aliasCol] = data["alias"] as String
				it[economyCol] = data["economy"] as Int
				it[industryCol] = data["industry"] as Int
				it[scienceCol] = data["science"] as Int
				it[starsCol] = data["stars"] as Int
				it[fleetCol] = data["fleet"] as Int
				it[shipsCol] = data["ships"] as Int
				it[isActiveCol] = data["isActive"] as Boolean
			}
			select(game = game, alias = data["alias"] as String)!!
		} else
			player
	}

	fun mapToPlayer(game: Game, data: Map<String, Any?>): Player = Util.query {
		val alias: String = data["alias"] as String
		val economy = (data["total_economy"] as Double).roundToInt()
		val industry = (data["total_industry"] as Double).roundToInt()
		val science = (data["total_science"] as Double).roundToInt()
		val stars = (data["total_stars"] as Double).roundToInt()
		val fleet = (data["total_fleets"] as Double).roundToInt()
		val ships = (data["total_strength"] as Double).roundToInt()
		val isActive = (data["conceded"] as Double).roundToInt() == 0
		Player(
			game = game,
			alias = alias,
			economy = economy,
			industry = industry,
			science = science,
			stars = stars,
			fleet = fleet,
			ships = ships,
			isActive = isActive
		)
	}

	private fun ResultRow.parse() = Player(
		game = GameTable.select(ID = this[gameCol].value)!!,
		team = TeamTable.select(ID = this[teamCol]?.value ?: -1),
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
}