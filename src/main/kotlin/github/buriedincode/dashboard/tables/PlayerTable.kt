package github.buriedincode.dashboard.tables

import github.buriedincode.dashboard.Utils
import org.apache.logging.log4j.kotlin.Logging
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SchemaUtils

object PlayerTable : LongIdTable(name = "players"), Logging {
    val gameCol: Column<EntityID<Long>> = reference(
        name = "game_id",
        foreign = GameTable,
        onUpdate = ReferenceOption.CASCADE,
        onDelete = ReferenceOption.CASCADE,
    )
    val playerIdCol: Column<Int> = integer(name = "player_id")
    val isActiveCol: Column<Boolean> = bool(name = "is_active")
    val usernameCol: Column<String> = text(name = "username")

    init {
        Utils.query {
            uniqueIndex(gameCol, playerIdCol)
            SchemaUtils.create(this)
        }
    }
}
