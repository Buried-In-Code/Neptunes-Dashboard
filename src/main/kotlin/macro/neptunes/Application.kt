package macro.neptunes

import macro.neptunes.game.Game
import macro.neptunes.player.Player
import macro.neptunes.team.Team
import org.apache.logging.log4j.LogManager
import kotlin.system.exitProcess

/**
 * Created by Macro303 on 2018-Nov-08.
 */
object Application {
	private val LOGGER = LogManager.getLogger(Application::class.java)
	private val game: Game by lazy {
		getGameData()
	}

	init {
		if (Config.gameID == null) {
			LOGGER.fatal("Requires a Game ID")
			exitProcess(0)
		}
	}

	@JvmStatic
	fun main(args: Array<String>) {
		LOGGER.info("Welcome To Neptune's Pride: ${game.name}")
		do {
			var players = getPlayerData()
			var teams = createTeams(players = players)
			displayTeamLeaderboard(teams.sortedWith(compareBy({ -it.calculateComplete(game = game) }, { it.name })))
			displayPlayerLeaderboard(players.sortedWith(compareBy({ -it.calculateComplete(game = game) }, { it.alias })))
			teams = teams.sortedWith(compareBy({ -it.calculateComplete(game = game) }, { it.name }))
			if (teams.first().calculateComplete(game = game) > 50.0) {
				LOGGER.info("Winner is: ${teams.first().name} with ${"%.2f".format(teams.first().calculateComplete(game = game))}%")
				players = players.sortedWith(compareBy({ -it.calculateComplete(game = game) }, { it.alias }))
				players.stream().filter { it.calculateComplete(game = game) > 12.5 }.forEach {
					LOGGER.info("${it.alias} was instrumental in the victory with ${"%.2f".format(it.calculateComplete(game = game))}%")
				}
				break
			}
			Thread.sleep(1000)
		}while (true)
	}

	@Suppress("UNCHECKED_CAST")
	private fun getGameData(): Game {
		val response = RestClient.getRequest(endpoint = "/basic")
		val game = Game.parse(response["Data"] as Map<String, Any?>)
		if (game == null) {
			LOGGER.fatal("Unable to find game")
			exitProcess(0)
		}
		return game
	}

	private fun createTeams(players: List<Player>): List<Team> {
		val teams = ArrayList<Team>()
		Config.players.forEach { teamName, value ->
			val team = Team(teamName)
			teams.add(team)
			value.forEach { alias, _ ->
				players.forEach {
					if (it.alias == alias)
						team.members.add(it)
				}
			}
		}
		return teams
	}

	@Suppress("UNCHECKED_CAST")
	private fun getPlayerData(): List<Player> {
		val players = ArrayList<Player>()
		val response = RestClient.getRequest(endpoint = "/players")
		(response["Data"] as Map<String, Any?>).values.forEach {
			val player = Player.parse(it as Map<String, Any?>)
			player ?: return@forEach
			LOGGER.debug("Loaded Player: ${player.alias}")
			players.add(player)
		}
		return players
	}

	private fun displayTeamLeaderboard(teams: List<Team>) {
		val format = "| %-9s | %9s | %5s | %5s |%n"
		println("+-----------+-----------+-------+-------+")
		println("| Team Name | Victory % | Stars | Fleet |")
		println("+-----------+-----------+-------+-------+")
		teams.forEach {
			System.out.printf(format, it.name, "%.2f".format(it.calculateComplete(game = game)) + "%", it.totalStars, it.totalFleet)
		}
		println("+-----------+-----------+-------+-------+")
	}

	private fun displayPlayerLeaderboard(players: List<Player>) {
		val format = "| %-20s | %9s | %5s | %5s |%n"
		println("+----------------------+-----------+-------+-------+")
		println("| Alias                | Victory % | Stars | Fleet |")
		println("+----------------------+-----------+-------+-------+")
		players.forEach {
			System.out.printf(format, it.alias, "%.2f".format(it.calculateComplete(game = game)) + "%", it.stars, it.fleet)
		}
		println("+----------------------+-----------+-------+-------+")
	}
}