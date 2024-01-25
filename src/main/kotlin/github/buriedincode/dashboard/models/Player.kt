package github.buriedincode.dashboard.models

import github.buriedincode.dashboard.tables.PlayerTable
import github.buriedincode.dashboard.tables.TurnTable
import org.apache.logging.log4j.kotlin.Logging
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Player(id: EntityID<Long>) : LongEntity(id), IJson, Comparable<Player> {
    companion object : LongEntityClass<Player>(PlayerTable), Logging {
        val comparator = compareBy(Player::username)
    }

    var game: Game by Game referencedOn PlayerTable.gameCol
    var playerId: Int by PlayerTable.playerIdCol

    var isActive: Boolean by PlayerTable.isActiveCol
    var username: String by PlayerTable.usernameCol

    val turns by Turn referrersOn TurnTable.playerCol

    override fun toJson(showAll: Boolean): Map<String, Any?> {
        val output = mutableMapOf<String, Any?>(
            "id" to id.value,
            "isActive" to isActive,
            "username" to username,
        )
        if (showAll) {
            output["game"] = game.toJson()
            output["turns"] = turns.sorted().map { it.toJson() }
        } else {
            output["gameId"] = game.id.value
        }
        return output.toSortedMap()
    }

    override fun compareTo(other: Player): Int = comparator.compare(this, other)
}
