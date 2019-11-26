package macro.dashboard.neptunes.player

import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import macro.dashboard.neptunes.IEntry
import macro.dashboard.neptunes.ISendable
import macro.dashboard.neptunes.tick.Tick
import macro.dashboard.neptunes.tick.TickTable
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.team.Team
import macro.dashboard.neptunes.team.TeamTable
import org.apache.logging.log4j.LogManager
import java.util.*

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Player(
	val gameId: Long,
	val alias: String,
	var teamId: UUID,
	var name: String? = null
) : ISendable, IEntry {
	@KtorExperimentalAPI
	override fun toJson(full: Boolean): Map<String, Any?> {
		val output = mutableMapOf<String, Any?>(
			"alias" to alias,
			"name" to name
		)
		if (full) {
			output["game"] = getGame().toJson(full = false)
			output["team"] = getTeam().toJson(full = false)
			output["ticks"] = getTicks().map { it.toJson(full = false) }
		}
		return output.toSortedMap()
	}

	override fun insert(): Player {
		PlayerTable.insert(item = this)
		return this
	}

	override fun update(): Player {
		PlayerTable.update(item = this)
		return this
	}

	override fun delete() {
		PlayerTable.delete(item = this)
	}

	@KtorExperimentalAPI
	fun getGame(): Game = GameTable.select(gameId = gameId) ?: throw NotFoundException()

	@KtorExperimentalAPI
	fun getTeam(): Team = TeamTable.select(uuid = teamId) ?: throw NotFoundException()

	fun getTicks(): List<Tick> = TickTable.search(gameId = gameId, playerId = alias)

	companion object {
		private val LOGGER = LogManager.getLogger(Player::class.java)
	}
}