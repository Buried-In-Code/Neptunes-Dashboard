package macro.neptunes.core

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import macro.neptunes.core.game.Game
import macro.neptunes.core.player.Player
import macro.neptunes.core.team.Team
import org.apache.logging.log4j.LogManager
import java.io.File
import java.time.LocalDateTime

/**
 * Created by Macro303 on 2018-Nov-12.
 */
object Util {
	private val LOGGER = LogManager.getLogger(Util::class.java)
	private val GSON = GsonBuilder()
			.serializeNulls()
			.disableHtmlEscaping()
			.create()
	internal const val ENDPOINT = "http://nptriton.cqproject.net/game/"
	lateinit var game: Game
	lateinit var players: List<Player>
	lateinit var lastUpdate: LocalDateTime
	var teams: List<Team>? = null

	val BIN: File by lazy {
		val temp = File("bin")
		if (!temp.exists()) {
			LOGGER.info("Bin Folder is missing, creating `$temp`")
			temp.mkdirs()
		}
		temp
	}

	internal fun sortPlayersByStars(players: List<Player>): List<Player> {
		return players.sortedWith(compareBy({ !it.isActive }, { -it.calcComplete() }, { -it.stars }, { it.name }, { it.alias }))
	}

	internal fun sortPlayersByName(players: List<Player>): List<Player> {
		return players.sortedWith(compareBy({ !it.isActive }, { it.name }, { it.alias }))
	}

	internal fun sortPlayersByAlias(players: List<Player>): List<Player> {
		return players.sortedWith(compareBy({ !it.isActive }, { it.alias }, { it.name }))
	}

	internal fun sortPlayersByTeam(players: List<Player>): List<Player> {
		return players.sortedWith(compareBy({ !it.isActive }, { it.team }, { it.name }, { it.alias }))
	}

	internal fun sortPlayersByShips(players: List<Player>): List<Player> {
		return players.sortedWith(compareBy({ !it.isActive }, { it.strength }, { it.name }, { it.alias }))
	}

	internal fun sortTeamsByWin(teams: List<Team>): List<Team> {
		return teams.sortedWith(compareBy({ !it.isActive }, { -it.calcComplete() }, { -it.totalStars }, { -it.totalStrength }, { it.name }))
	}

	internal fun sortTeamsByName(teams: List<Team>): List<Team> {
		return teams.sortedWith(compareBy({ !it.isActive }, { it.name }))
	}

	@Throws(JsonSyntaxException::class)
	internal fun String.fromJSON(): Map<String, Any> {
		if (this.isBlank()) return emptyMap()
		val typeOfHashMap = object : TypeToken<Map<String, Any>>() {
		}.type
		return GSON.fromJson(this, typeOfHashMap)
	}

	internal fun Any?.toJSON(): String = GSON.toJson(this)

	internal fun htmlToString(location: String): String{
		return Util::class.java.getResource("/public/$location.html").readText()
	}
}