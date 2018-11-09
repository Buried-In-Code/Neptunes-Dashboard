package macro.neptunes.core

import macro.neptunes.core.game.Game
import macro.neptunes.core.player.Player
import macro.neptunes.core.team.Team
import org.apache.logging.log4j.LogManager
import java.io.File
import java.text.DecimalFormat

object Util {
	private val LOGGER = LogManager.getLogger(Util::class.java)
	internal val PERCENT_FORMAT = DecimalFormat("00.0")

	val BIN: File by lazy {
		val temp = File("bin")
		if (!temp.exists()) {
			LOGGER.info("Bin Folder is missing, creating `$temp`")
			temp.mkdirs()
		}
		temp
	}

	internal fun sortPlayerList(players: List<Player>, game: Game): List<Player> {
		return players.sortedWith(compareBy({ !it.isActive }, { -it.calcComplete(game = game) }, { -it.stars }, { -it.strength }, { it.alias }))
	}

	internal fun sortTeamList(teams: List<Team>, game: Game): List<Team> {
		return teams.sortedWith(compareBy({ !it.isActive }, { -it.calcComplete(game = game) }, { -it.totalStars }, { -it.totalStrength }, { it.name }))
	}
}