package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.team.TeamTable
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import java.util.*

/**
 * Created by Macro303 on 2019-Feb-11.
 */
internal object PlayerTable : Table(name = "Player") {
	val gameCol = reference(
		name = "gameId",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val aliasCol = text(name = "alias")
	val teamCol = reference(
		name = "teamId",
		foreign = TeamTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val nameCol = text(name = "name").nullable()

	private val LOGGER = LogManager.getLogger(PlayerTable::class.java)

	init {
		Util.query(description = "Create Player table") {
			uniqueIndex(gameCol, aliasCol)
			SchemaUtils.create(this)
		}
	}

	fun select(gameId: Long, alias: String): Player? =
		Util.query(description = "Select Player by GameId: $gameId, Alias: $alias") {
			select {
				gameCol eq gameId and (aliasCol eq alias)
			}.limit(1).firstOrNull()?.parse()
		}

	fun search(gameId: Long, teamId: UUID): List<Player> =
		Util.query(description = "Search Players in Game: $gameId, from Team $teamId") {
			select {
				gameCol eq gameId and (teamCol eq teamId.toString())
			}.map { it.parse() }
		}

	fun search(gameId: Long): List<Player> = Util.query(description = "Search Players in Game: $gameId") {
		select {
			gameCol eq gameId
		}.map { it.parse() }
	}

	fun insert(item: Player): Boolean = Util.query(description = "Insert Player") {
		try {
			insert {
				it[gameCol] = EntityID(item.gameId, GameTable)
				it[aliasCol] = item.alias
				it[teamCol] = EntityID(item.teamId.toString(), TeamTable)
				it[nameCol] = item.name
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun update(item: Player): Boolean = Util.query(description = "Update Player") {
		try {
			update({ gameCol eq item.gameId and (aliasCol eq item.alias) }) {
				it[teamCol] = EntityID(item.teamId.toString(), TeamTable)
				it[nameCol] = item.name
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun delete(item: Player): Boolean = Util.query(description = "Delete Player") {
		try {
			deleteWhere { gameCol eq item.gameId and (aliasCol eq item.alias) }
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	private fun ResultRow.parse(): Player = Player(
		gameId = this[gameCol].value,
		alias = this[aliasCol],
		teamId = UUID.fromString(this[teamCol].value),
		name = this[nameCol]
	)
}