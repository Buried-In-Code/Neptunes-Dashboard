package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.backend.ProteusTech
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2019-Mar-08.
 */
object TechnologyTable : IntIdTable(name = "Technology") {
	private val turnCol = reference(
		name = "turnID",
		foreign = TurnTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	private val nameCol = text(name = "name")
	private val valueCol = double(name = "value")
	private val levelCol = integer(name = "level")

	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	init {
		Util.query(description = "Create Tech table") {
			uniqueIndex(turnCol, nameCol)
			SchemaUtils.create(this)
		}
	}

	fun select(ID: Int): Technology? = Util.query(description = "Select Tech by ID: $ID") {
		select {
			id eq ID
		}.orderBy(turnCol to SortOrder.ASC, nameCol to SortOrder.ASC).limit(n = 1).firstOrNull()?.parse()
	}

	fun select(turnID: Int, name: String): Technology? =
		Util.query(description = "Select Tech by Turn: $turnID and name: $name") {
			select {
				turnCol eq turnID and (nameCol like name)
			}.orderBy(turnCol to SortOrder.ASC, nameCol to SortOrder.ASC).limit(n = 1).firstOrNull()?.parse()
		}

	fun searchByTurn(turnID: Int): List<Technology> = Util.query(description = "Search for Techs at Turn: $turnID") {
		select {
			turnCol eq turnID
		}.orderBy(turnCol to SortOrder.ASC, nameCol to SortOrder.ASC).map {
			it.parse()
		}
	}

	fun insert(turnID: Int, name: String, update: ProteusTech): Boolean =
		Util.query(description = "Insert Proteus Tech") {
			try {
				insert {
					it[turnCol] = EntityID(id = turnID, table = TurnTable)
					it[nameCol] = name
					it[valueCol] = update.value
					it[levelCol] = update.level
				}
				true
			} catch (esqle: ExposedSQLException) {
				false
			}
		}

	private fun ResultRow.parse(): Technology =
		Technology(
			ID = this[id].value,
			turnID = this[turnCol].value,
			name = this[nameCol],
			value = this[valueCol],
			level = this[levelCol]
		)
}