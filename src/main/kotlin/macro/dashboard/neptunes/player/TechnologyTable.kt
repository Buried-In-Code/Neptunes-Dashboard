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
	private val cycleCol = reference(
		name = "turnID",
		foreign = CycleTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	private val nameCol = text(name = "name")
	private val valueCol = double(name = "value")
	private val levelCol = integer(name = "level")

	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	init {
		Util.query(description = "Create Tech table") {
			uniqueIndex(cycleCol, nameCol)
			SchemaUtils.create(this)
		}
	}

	fun select(ID: Int): Technology? = Util.query(description = "Select Tech by ID: $ID") {
		select {
			id eq ID
		}.orderBy(cycleCol to SortOrder.ASC, nameCol to SortOrder.ASC).limit(n = 1).firstOrNull()?.parse()
	}

	fun select(cycleID: Int, name: String): Technology? =
		Util.query(description = "Select Tech by Cycle: $cycleID and name: $name") {
			select {
				cycleCol eq cycleID and (nameCol like name)
			}.orderBy(cycleCol to SortOrder.ASC, nameCol to SortOrder.ASC).limit(n = 1).firstOrNull()?.parse()
		}

	fun searchByCycle(cycleID: Int): List<Technology> = Util.query(description = "Search for Techs at Cycle: $cycleID") {
		select {
			cycleCol eq cycleID
		}.orderBy(cycleCol to SortOrder.ASC, nameCol to SortOrder.ASC).map {
			it.parse()
		}
	}

	fun insert(cycleID: Int, name: String, update: ProteusTech): Boolean =
		Util.query(description = "Insert Proteus Tech") {
			try {
				insert {
					it[cycleCol] = EntityID(id = cycleID, table = CycleTable)
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
			cycleID = this[cycleCol].value,
			name = this[nameCol],
			value = this[valueCol],
			level = this[levelCol]
		)
}