package github.buriedincode.models

import github.buriedincode.Utils.transaction
import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SchemaUtils

class Player(id: EntityID<Long>) : LongEntity(id), IJson, Comparable<Player> {
  companion object : LongEntityClass<Player>(PlayerTable) {
    @JvmStatic private val LOGGER = KotlinLogging.logger {}

    val comparator = compareBy(Player::username)
  }

  var game: Game by Game referencedOn PlayerTable.gameCol
  var playerId: Int by PlayerTable.playerIdCol

  var isActive: Boolean by PlayerTable.isActiveCol
  var username: String by PlayerTable.usernameCol

  val turns by Turn referrersOn TurnTable.playerCol

  override fun toJson(showAll: Boolean): Map<String, Any?> {
    val output = mutableMapOf<String, Any?>("id" to id.value, "isActive" to isActive, "username" to username)
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

object PlayerTable : LongIdTable(name = "players") {
  @JvmStatic private val LOGGER = KotlinLogging.logger {}

  val gameCol: Column<EntityID<Long>> =
    reference(
      name = "game_id",
      foreign = GameTable,
      onUpdate = ReferenceOption.CASCADE,
      onDelete = ReferenceOption.CASCADE,
    )
  val playerIdCol: Column<Int> = integer(name = "player_id")
  val isActiveCol: Column<Boolean> = bool(name = "is_active")
  val usernameCol: Column<String> = text(name = "username")

  init {
    transaction {
      uniqueIndex(gameCol, playerIdCol)
      SchemaUtils.create(this)
    }
  }
}
