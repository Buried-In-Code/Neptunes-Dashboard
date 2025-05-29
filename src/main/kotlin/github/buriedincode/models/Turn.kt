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

class Turn(id: EntityID<Long>) : LongEntity(id), IJson, Comparable<Turn> {
  companion object : LongEntityClass<Turn>(TurnTable) {
    @JvmStatic private val LOGGER = KotlinLogging.logger {}

    val comparator = compareBy(Turn::player).thenBy(Turn::turn)
  }

  var player: Player by Player referencedOn TurnTable.playerCol
  var turn: Long by TurnTable.turnCol

  var industry: Int by TurnTable.industryCol
  var economy: Int by TurnTable.economyCol
  var science: Int by TurnTable.scienceCol
  var stars: Int by TurnTable.starsCol
  var ships: Int by TurnTable.shipsCol

  var scanning: Int by TurnTable.scanningCol
  var propulsion: Int by TurnTable.propulsionCol
  var terraforming: Int by TurnTable.terraformingCol
  var research: Int by TurnTable.researchCol
  var weapons: Int by TurnTable.weaponsCol
  var banking: Int by TurnTable.bankingCol
  var manufacturing: Int by TurnTable.manufacturingCol

  override fun toJson(showAll: Boolean): Map<String, Any?> {
    val output =
      mutableMapOf<String, Any?>(
        "id" to id.value,
        "industry" to industry,
        "economy" to economy,
        "science" to science,
        "stars" to stars,
        "ships" to ships,
        "technology" to
          mapOf(
              "scanning" to scanning,
              "propulsion" to propulsion,
              "terraforming" to terraforming,
              "research" to research,
              "weapons" to weapons,
              "banking" to banking,
              "manufacturing" to manufacturing,
            )
            .toSortedMap(),
        "turn" to turn,
      )
    if (showAll) {
      output["player"] = player.toJson()
    } else {
      output["playerId"] = player.id.value
    }
    return output.toSortedMap()
  }

  override fun compareTo(other: Turn): Int = comparator.compare(this, other)
}

object TurnTable : LongIdTable(name = "turns") {
  @JvmStatic private val LOGGER = KotlinLogging.logger {}

  val playerCol: Column<EntityID<Long>> =
    reference(
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
    transaction {
      uniqueIndex(playerCol, turnCol)
      SchemaUtils.create(this)
    }
  }
}
