package macro.dashboard.neptunes.team

import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import macro.dashboard.neptunes.IEntry
import macro.dashboard.neptunes.ISendable
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.Player
import macro.dashboard.neptunes.player.PlayerTable
import org.apache.logging.log4j.LogManager
import java.util.*

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Team(
	val uuid: UUID = UUID.randomUUID(),
	val gameId: Long,
	var name: String
) : ISendable, IEntry {
	@KtorExperimentalAPI
	override fun toJson(full: Boolean): Map<String, Any?> {
		val output = mutableMapOf<String, Any?>(
			"uuid" to uuid,
			"name" to name
		)
		if (full) {
			output["game"] = getGame().toJson(full = false)
			output["players"] = getPlayers().map { it.toJson(full = false) }
		}
		return output.toSortedMap()
	}

	override fun insert(): Team {
		TeamTable.insert(item = this)
		return this
	}

	override fun update(): Team {
		TeamTable.update(item = this)
		return this
	}

	override fun delete() {
		TeamTable.delete(item = this)
	}

	@KtorExperimentalAPI
	fun getGame(): Game = GameTable.select(gameId = gameId) ?: throw NotFoundException()

	fun getPlayers(): List<Player> = PlayerTable.search(gameId = gameId, teamId = uuid)

	companion object {
		private val LOGGER = LogManager.getLogger(Team::class.java)
	}
}