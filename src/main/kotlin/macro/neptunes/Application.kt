package macro.neptunes

import macro.neptunes.console.Console
import macro.neptunes.core.Config
import macro.neptunes.core.Util
import macro.neptunes.core.game.Game
import macro.neptunes.core.player.Player
import macro.neptunes.core.team.Team
import macro.neptunes.data.Parser
import macro.neptunes.data.RestClient
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
		Console.displayHeading(text = "Welcome To Neptune's Pride: ${game.name}")
		do {
			var players = getPlayerData()
			var teams = emptyList<Team>()
			if (Config.enableTeams)
				teams = createTeams(players = players)
			displayPlayerLeaderboard(players = players, game = game)
			if (Config.enableTeams) {
				displayTeamLeaderboard(teams = teams, game = game)
				teams = Util.sortTeamList(teams = teams, game = game)
				if (teams.firstOrNull()?.hasWon(game = game) == true) {
					Console.display("${Console.HEADING}${teams.first().name} ${Console.STANDARD}wins with ${Util.PERCENT_FORMAT.format(teams.first().calcComplete(game = game))}%")
					var teamMembers: List<Player> = players.filter { it.team == teams.first().name }
					teamMembers = teamMembers.filter { it.calcComplete(game = game) > Config.winPercentage.div(teamMembers.size) }
					Util.sortPlayerList(players = teamMembers, game = game).forEach {
						Console.display(text = "${Console.HEADING}${it.playerName()} ${Console.STANDARD}was instrumental in the team victory with ${Util.PERCENT_FORMAT.format(it.calcComplete(game = game))}%")
					}
					break
				}
			} else {
				players = Util.sortPlayerList(players = players, game = game)
				if (players.firstOrNull()?.hasWon(game = game) == true) {
					Console.display("${Console.HEADING}${players.first().playerName()} ${Console.STANDARD}wins with ${Util.PERCENT_FORMAT.format(players.first().calcComplete(game = game))}%")
					break
				}
			}
			Thread.sleep((Config.refreshRate * 1000).toLong())
		} while (true)
	}

	@Suppress("UNCHECKED_CAST")
	private fun getGameData(): Game {
		val response = RestClient.getRequest(endpoint = "/basic")
		val game = Parser.parseGame(data = response["Data"] as Map<String, Any?>)
		if (game == null) {
			LOGGER.fatal("Unable to find game")
			exitProcess(status = 0)
		}
		return game
	}

	private fun createTeams(players: List<Player>): List<Team> {
		val teams = ArrayList<Team>()
		Config.teams.forEach { key, value ->
			val team = Team(name = key)
			players.forEach { player ->
				if (value.contains(player.name)) {
					player.team = key
					team.members.add(player)
				}
			}
			LOGGER.info("Loaded Team: ${team.name}")
			teams.add(team)
		}
		return teams
	}

	@Suppress("UNCHECKED_CAST")
	private fun getPlayerData(): List<Player> {
		val players = ArrayList<Player>()
		val response = RestClient.getRequest(endpoint = "/players")
		(response["Data"] as Map<String, Any?>).values.forEach {
			val player = Parser.parsePlayer(data = it as Map<String, Any?>)
			player ?: return@forEach
			LOGGER.info("Loaded Player: ${player.alias}")
			players.add(player)
		}
		return players
	}

	private fun displayTeamLeaderboard(teams: List<Team>, game: Game) {
		val headers = listOf("Team", "Stars", "Ships", "Economy", "$/Turn", "Industry", "Ships/Turn", "Science", "Active")
		val data = ArrayList<List<Any>>()
		val teamData = Util.sortTeamList(teams = teams, game = game)
		teamData.forEach {
			data.add(listOf(it.name, "${it.totalStars} (${Util.PERCENT_FORMAT.format(it.calcComplete(game = game))}%)", it.totalStrength, it.totalEconomy, it.calcMoney(), it.totalIndustry, it.calcShips(), it.totalScience, it.isActive))
		}
		Console.displayLeaderboard(headers = headers, data = data)
	}

	private fun displayPlayerLeaderboard(players: List<Player>, game: Game) {
		val headers: List<String> = if (Config.enableTeams)
			listOf("Player", "Team", "Stars", "Ships", "Economy", "$/Turn", "Industry", "Ships/Turn", "Science", "Banking", "Experimentation", "Hyperspace", "Manufacturing", "Scanning", "Terraforming", "Weapons", "Active")
		else
			listOf("Player", "Stars", "Ships", "Economy", "$/Turn", "Industry", "Ships/Turn", "Science", "Banking", "Experimentation", "Hyperspace", "Manufacturing", "Scanning", "Terraforming", "Weapons", "Active")
		val data = ArrayList<List<Any>>()
		val playerData = Util.sortPlayerList(players = players, game = game)
		if (Config.enableTeams)
			playerData.forEach {
				data.add(listOf(it.playerName(), it.team, "${it.stars} (${Util.PERCENT_FORMAT.format(it.calcComplete(game = game))}%)", it.strength, it.economy, it.calcMoney(), it.industry, it.calcShips(), it.science, it.banking, it.experimentation, it.hyperspace, it.manufacturing, it.scanning, it.terraforming, it.weapons, it.isActive))
			}
		else
			playerData.forEach {
				data.add(listOf(it.playerName(), "${it.stars} (${Util.PERCENT_FORMAT.format(it.calcComplete(game = game))}%)", it.strength, it.economy, it.calcMoney(), it.industry, it.calcShips(), it.science, it.banking, it.experimentation, it.hyperspace, it.manufacturing, it.scanning, it.terraforming, it.weapons, it.isActive))
			}
		Console.displayLeaderboard(headers = headers, data = data)
	}
}