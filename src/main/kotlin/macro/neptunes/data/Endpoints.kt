package macro.neptunes.data

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object Endpoints {
	const val WELCOME = "/"
	const val GAME = "/game"
	const val PLAYERS = "/players"
	const val PLAYERS_LEADERBOARD = "$PLAYERS/leaderboard"
	const val PLAYER = "$PLAYERS/:alias"
	const val TEAMS = "/teams"
	const val TEAMS_LEADERBOARD = "$TEAMS/leaderboard"
	const val TEAM = "$TEAMS/:name"
	const val REFRESH = "/refresh"
	const val CONFIG = "/config"
	const val HELP = "/help"
}