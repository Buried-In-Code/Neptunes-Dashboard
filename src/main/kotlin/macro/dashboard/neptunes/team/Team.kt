package macro.dashboard.neptunes.team

import macro.dashboard.neptunes.ISendable
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.player.Player
import macro.dashboard.neptunes.player.PlayerTable
import org.jetbrains.exposed.dao.*
import java.util.*

/**
 * Created by Macro303 on 2018-Nov-08.
 */
class Team(id: EntityID<Long>) : LongEntity(id), ISendable {
	companion object : LongEntityClass<Team>(TeamTable)

	var game by Game referencedOn TeamTable.gameCol
	var name by TeamTable.nameCol

	val players by Player referrersOn PlayerTable.teamCol

	override fun toJson(full: Boolean): Map<String, Any?> {
		val output = mutableMapOf<String, Any?>(
			"id" to id.value,
			"name" to name
		)
		if (full) {
			output["game"] = game.toJson(full = false)
			output["players"] = players.map { it.toJson(full = false) }
		}
		return output.toSortedMap()
	}
}