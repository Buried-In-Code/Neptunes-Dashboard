package macro.neptunes.data

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object Endpoints {
	const val WEB = "/web"
	const val API = "/api"

	const val WELCOME = "/"
	const val GAME = "/game"
	const val PLAYERS = "/players"
	const val PLAYER = "/players/:alias"
	const val PLAYER_LEADERBOARD = "$PLAYERS/leaderboard"
	const val TEAMS = "/teams"
	const val TEAM = "/teams/:name"
	const val TEAM_LEADERBOARD = "$TEAMS/leaderboard"
	const val REFRESH = "/refresh"
	const val CONFIG = "/config"
	const val HELP = "/help"
}