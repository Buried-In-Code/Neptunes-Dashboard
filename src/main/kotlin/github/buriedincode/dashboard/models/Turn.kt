package github.buriedincode.dashboard.models

import github.buriedincode.dashboard.tables.TurnTable
import org.apache.logging.log4j.kotlin.Logging
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Turn(id: EntityID<Long>) : LongEntity(id), IJson, Comparable<Turn> {
    companion object : LongEntityClass<Turn>(TurnTable), Logging {
        val comparator = compareBy(Turn::player)
            .thenBy(Turn::turn)
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
        val output = mutableMapOf<String, Any?>(
            "id" to id.value,
            "industry" to industry,
            "economy" to economy,
            "science" to science,
            "stars" to stars,
            "ships" to ships,
            "technology" to mapOf(
                "scanning" to scanning,
                "propulsion" to propulsion,
                "terraforming" to terraforming,
                "research" to research,
                "weapons" to weapons,
                "banking" to banking,
                "manufacturing" to manufacturing,
            ).toSortedMap(),
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
