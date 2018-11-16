package macro.neptunes

import com.google.gson.GsonBuilder
import io.javalin.Context
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.FromJsonMapper
import io.javalin.json.JavalinJson
import io.javalin.json.ToJsonMapper
import io.javalin.security.Role
import io.javalin.security.SecurityUtil.roles
import macro.neptunes.Application.SecurityRoles.*
import macro.neptunes.core.Config
import macro.neptunes.core.Util
import macro.neptunes.core.game.GameController
import macro.neptunes.core.game.GameHandler
import macro.neptunes.core.player.PlayerController
import macro.neptunes.core.player.PlayerHandler
import macro.neptunes.core.team.TeamController
import macro.neptunes.core.team.TeamHandler
import macro.neptunes.data.*
import org.apache.logging.log4j.LogManager
import java.time.Duration
import java.time.LocalDateTime
import kotlin.system.exitProcess

/**
 * Created by Macro303 on 2018-Nov-12.
 */
object Application {
	private val LOGGER = LogManager.getLogger(Application::class.java)
	private val GSON = GsonBuilder()
		.serializeNulls()
		.disableHtmlEscaping()
		.create()
	const val JSON = "application/json"
	const val HTML = "text/HTML"

	init {
		if (Config.gameID == null) {
			LOGGER.fatal("Requires a Game ID")
			exitProcess(0)
		}
		refreshData()
	}

	@JvmStatic
	fun main(args: Array<String>) {
		JavalinJson.fromJsonMapper = object : FromJsonMapper {
			override fun <T> map(json: String, targetClass: Class<T>): T {
				return GSON.fromJson(json, targetClass)
			}
		}
		JavalinJson.toJsonMapper = object : ToJsonMapper {
			override fun map(obj: Any): String {
				return GSON.toJson(obj)
			}
		}
		val app = Javalin.create().apply {
			port(Config.port)
			accessManager { handler, context, permittedRoles ->
				val userRole = getUserRole(context = context)
				LOGGER.warn("User access level: $userRole")
				if (permittedRoles.contains(userRole))
					handler.handle(context)
				else
					Exceptions.illegalAccess(context = context)
			}
		}.start()

		app.before("/api*") {
			if (it.header("Content-Type") != JSON)
				Exceptions.contentType(context = it)
		}
		app.before {
			when (it.method()) {
				"HEAD" -> LOGGER.info(
					"${it.protocol()} ${it.method()} >> Endpoint: ${it.path()}, Content-Type: ${it.header(
						"Content-Type"
					)}, Access: ${it.header("Access")}"
				)
				"GET" -> LOGGER.info(
					"${it.protocol()} ${it.method()} >> Endpoint: ${it.path()}, Content-Type: ${it.header(
						"Content-Type"
					)}, Access: ${it.header("Access")}"
				)
				"POST" -> LOGGER.info(
					"${it.protocol()} ${it.method()} >> Endpoint: ${it.path()}, Content-Type: ${it.header(
						"Content-Type"
					)}, Access: ${it.header("Access")}, Body: ${it.body()}"
				)
			}
			val now: LocalDateTime = LocalDateTime.now()
			val difference: Duration = Duration.between(Util.lastUpdate, now)
			if (difference.toMinutes() > Config.refreshRate)
				refreshData()
		}
		app.after {
			LOGGER.info("${it.protocol()} ${it.status()} << Content-Type: ${it.header("Content-Type")}")
		}

		app.routes {
			path(Endpoints.WEB) {
				get(Endpoints.WELCOME, WelcomeController::webGet, roles(EVERYONE, DEVELOPER, ADMIN))
				get(Endpoints.GAME, GameController::webGet, roles(EVERYONE, DEVELOPER, ADMIN))
				get(
					Endpoints.PLAYER_LEADERBOARD,
					PlayerController.Leaderboard::webGet,
					roles(EVERYONE, DEVELOPER, ADMIN)
				)
				get(Endpoints.PLAYERS, PlayerController::webGetAll, roles(EVERYONE, DEVELOPER, ADMIN))
				get(Endpoints.PLAYER, PlayerController::webGet, roles(EVERYONE, DEVELOPER, ADMIN))
				get(Endpoints.TEAM_LEADERBOARD, TeamController.Leaderboard::webGet, roles(EVERYONE, DEVELOPER, ADMIN))
				get(Endpoints.TEAMS, TeamController::webGetAll, roles(EVERYONE, DEVELOPER, ADMIN))
				get(Endpoints.TEAM, TeamController::webGet, roles(EVERYONE, DEVELOPER, ADMIN))
				get(Endpoints.HELP, HelpController::get, roles(EVERYONE, DEVELOPER, ADMIN))
			}
		}

		app.routes {
			path(Endpoints.API) {
				get(Endpoints.WELCOME, WelcomeController::apiGet, roles(DEVELOPER, ADMIN))
				get(Endpoints.GAME, GameController::apiGet, roles(DEVELOPER, ADMIN))
				get(Endpoints.PLAYER_LEADERBOARD, PlayerController.Leaderboard::apiGet, roles(DEVELOPER, ADMIN))
				get(Endpoints.PLAYERS, PlayerController::apiGetAll, roles(DEVELOPER, ADMIN))
				post(Endpoints.PLAYERS, PlayerController::apiPost, roles(DEVELOPER, ADMIN))
				get(Endpoints.PLAYER, PlayerController::apiGet, roles(DEVELOPER, ADMIN))
				get(Endpoints.TEAM_LEADERBOARD, TeamController.Leaderboard::apiGet, roles(DEVELOPER, ADMIN))
				get(Endpoints.TEAMS, TeamController::apiGetAll, roles(DEVELOPER, ADMIN))
				post(Endpoints.TEAMS, TeamController::apiPost, roles(DEVELOPER, ADMIN))
				get(Endpoints.TEAM, TeamController::apiGet, roles(DEVELOPER, ADMIN))
				get(Endpoints.REFRESH, {
					if (it.status() < 400) {
						refreshData()
						it.status(204)
					}
				}, roles(DEVELOPER, ADMIN))
				get(Endpoints.CONFIG, {
					if (it.status() < 400)
						Exceptions.notYetAvailable(context = it)
				}, roles(DEVELOPER, ADMIN))
				patch(Endpoints.CONFIG, {
					if (it.status() < 400)
						Exceptions.notYetAvailable(context = it)
				}, roles(ADMIN))
				get(Endpoints.HELP, HelpController::get, roles(EVERYONE, DEVELOPER, ADMIN))
			}
		}
	}

	fun refreshData() {
		GameHandler.refreshData()
		PlayerHandler.refreshData()
		if (Config.enableTeams)
			TeamHandler.refreshData()
		else
			TeamHandler.teams = null
		Util.lastUpdate = LocalDateTime.now()
	}

	private fun getUserRole(context: Context): Role {
		return when (context.header("Access")?.toLowerCase()) {
			"admin" -> ADMIN
			"dev" -> DEVELOPER
			else -> EVERYONE
		}
	}

	private enum class SecurityRoles : Role {
		EVERYONE,
		DEVELOPER,
		ADMIN
	}
}