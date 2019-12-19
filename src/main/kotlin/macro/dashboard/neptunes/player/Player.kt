package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.ISendable
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.team.Team
import macro.dashboard.neptunes.tick.Tick
import macro.dashboard.neptunes.tick.TickTable
import org.jetbrains.exposed.dao.*
import java.util.*

/**
 * Created by Macro303 on 2018-Nov-08.
 */
class Player(id: EntityID<Long>) : LongEntity(id), ISendable {
	companion object : LongEntityClass<Player>(PlayerTable)

	var game by Game referencedOn PlayerTable.gameCol
	var alias by PlayerTable.aliasCol
	var team by Team referencedOn PlayerTable.teamCol
	var name by PlayerTable.nameCol

	val ticks by Tick referrersOn TickTable.playerCol

	override fun toJson(full: Boolean): Map<String, Any?> {
		val output = mutableMapOf<String, Any?>(
			"id" to id.value,
			"alias" to alias,
			"name" to name
		)
		if (full) {
			output["game"] = game.toJson(full = false)
			output["team"] = team.toJson(full = false)
			output["ticks"] = ticks.map { it.toJson(full = false) }
		}
		return output.toSortedMap()
	}
}