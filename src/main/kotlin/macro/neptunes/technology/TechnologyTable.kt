package macro.neptunes.technology

import macro.neptunes.Util
import macro.neptunes.player.Player
import macro.neptunes.player.PlayerTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*

/**
 * Created by Macro303 on 2019-Feb-25.
 */
object TechnologyTable : Table(name = "Technology") {
	private val playerCol: Column<String> = reference(
		name = "playerAlias",
		refColumn = PlayerTable.aliasCol,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	private val nameCol: Column<String> = text(name = "name")
	private val levelCol: Column<Int> = integer(name = "level").default(0)
	private val valueCol: Column<Double> = double(name = "value").default(0.0)

	init {
		Util.query {
			uniqueIndex(playerCol, nameCol)
			SchemaUtils.create(this)
		}
	}

	fun search(): List<Technology> = Util.query {
		selectAll().map {
			it.parse()
		}
	}

	fun search(player: Player): List<Technology> = Util.query {
		select {
			playerCol eq player.alias
		}.map {
			it.parse()
		}
	}

	fun search(player: Player, name: String): Technology? = Util.query {
		select {
			playerCol eq player.alias and (nameCol eq name)
		}.map {
			it.parse()
		}.firstOrNull()
	}

	fun insert(player: Player, name: String, level: Int, value: Double): Boolean = Util.query {
		try {
			insert {
				it[playerCol] = player.alias
				it[nameCol] = name
				it[levelCol] = level
				it[valueCol] = value
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun insert(player: Player, technology: Technology): Boolean = Util.query {
		insert(
			player = player,
			name = technology.name,
			level = technology.level,
			value = technology.value
		)
	}

	fun update(player: Player, technology: Technology): Boolean = Util.query {
		try {
			update({ playerCol eq player.alias and (nameCol eq technology.name) }) {
				it[levelCol] = technology.level
				it[valueCol] = technology.value
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	private fun ResultRow.parse(): Technology = Technology(
		name = this[nameCol],
		level = this[levelCol],
		value = this[valueCol]
	)

	fun Technology.update(
		player: Player,
		level: Int = this.level,
		value: Double = this.value
	): Boolean {
		this.level = level
		this.value = value
		return update(player = player, technology = this)
	}
}