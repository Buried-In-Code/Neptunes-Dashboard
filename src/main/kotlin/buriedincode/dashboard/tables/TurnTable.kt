package github.buriedincode.dashboard.tables

import github.buriedincode.dashboard.Utils
import org.apache.logging.log4j.kotlin.Logging
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SchemaUtils

object TurnTable : LongIdTable(name = "turns"), Logging {
    val playerCol: Column<EntityID<Long>> = reference(
        name = "player_id",
        foreign = PlayerTable,
        onUpdate = ReferenceOption.CASCADE,
        onDelete = ReferenceOption.CASCADE,
    )
    val turnCol: Column<Long> = long(name = "turn")
    val industryCol: Column<Int> = integer(name = "industry")
    val economyCol: Column<Int> = integer(name = "economy")
    val scienceCol: Column<Int> = integer(name = "science")
    val starsCol: Column<Int> = integer(name = "stars")
    val shipsCol: Column<Int> = integer(name = "ships")
    val scanningCol: Column<Int> = integer(name = "scanning")
    val propulsionCol: Column<Int> = integer(name = "propulsion")
    val terraformingCol: Column<Int> = integer(name = "terraforming")
    val researchCol: Column<Int> = integer(name = "research")
    val weaponsCol: Column<Int> = integer(name = "weapons")
    val bankingCol: Column<Int> = integer(name = "banking")
    val manufacturingCol: Column<Int> = integer(name = "manufacturing")

    init {
        Utils.query {
            uniqueIndex(playerCol, turnCol)
            SchemaUtils.create(this)
        }
    }
}
