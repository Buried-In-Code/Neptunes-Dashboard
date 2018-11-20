package macro.neptunes

import com.google.gson.GsonBuilder
import io.javalin.Context
import io.javalin.Javalin
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
import macro.neptunes.data.Endpoints
import macro.neptunes.data.Exceptions
import macro.neptunes.data.HelpController
import macro.neptunes.data.WelcomeController
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

		app.before {
			when (it.method()) {
				"HEAD" -> LOGGER.info(
					"${it.protocol()} ${it.method()} >> Endpoint: ${it.path()}, Content-Type: ${it.header(
						header = "Content-Type"
					)}, Access: ${it.header(header = "Access")}"
				)
				"GET" -> LOGGER.info(
					"${it.protocol()} ${it.method()} >> Endpoint: ${it.path()}, Content-Type: ${it.header(
						header = "Content-Type"
					)}, Access: ${it.header(header = "Access")}"
				)
				"POST" -> LOGGER.info(
					"${it.protocol()} ${it.method()} >> Endpoint: ${it.path()}, Content-Type: ${it.header(
						header = "Content-Type"
					)}, Access: ${it.header(header = "Access")}, Body: ${it.body()}"
				)
			}
			val now: LocalDateTime = LocalDateTime.now()
			val difference: Duration = Duration.between(Util.lastUpdate, now)
			if (difference.toMinutes() > Config.refreshRate)
				refreshData()
		}
		app.after {
			LOGGER.info("${it.protocol()} ${it.status()} << Content-Type: ${it.header(header = "Content-Type")}")
		}

		app.get(Endpoints.WELCOME, {
			if (it.header(header = "Content-Type") == JSON)
				WelcomeController.apiGet(context = it)
			else
				WelcomeController.webGet(context = it)
		}, roles(EVERYONE, DEVELOPER, ADMIN))
		app.get(Endpoints.GAME, {
			if (it.header(header = "Content-Type") == JSON)
				GameController.apiGet(context = it)
			else
				GameController.webGet(context = it)
		}, roles(EVERYONE, DEVELOPER, ADMIN))
		app.get(Endpoints.PLAYERS_LEADERBOARD, {
			if (it.header(header = "Content-Type") == JSON)
				PlayerController.Leaderboard.apiGet(context = it)
			else
				PlayerController.Leaderboard.webGet(context = it)
		}, roles(EVERYONE, DEVELOPER, ADMIN))
		app.get(Endpoints.PLAYERS, {
			if (it.header(header = "Content-Type") == JSON)
				PlayerController.apiGetAll(context = it)
			else
				PlayerController.webGetAll(context = it)
		}, roles(EVERYONE, DEVELOPER, ADMIN))
		app.post(Endpoints.PLAYERS, {
			if (it.header(header = "Content-Type") == JSON)
				PlayerController.apiPost(context = it)
		}, roles(DEVELOPER, ADMIN))
		app.get(Endpoints.PLAYER, {
			if (it.header(header = "Content-Type") == JSON)
				PlayerController.apiGet(context = it)
			else
				PlayerController.webGet(context = it)
		}, roles(EVERYONE, DEVELOPER, ADMIN))
		app.get(Endpoints.TEAMS_LEADERBOARD, {
			if (it.header(header = "Content-Type") == JSON)
				TeamController.Leaderboard.apiGet(context = it)
			else
				TeamController.Leaderboard.webGet(context = it)
		}, roles(EVERYONE, DEVELOPER, ADMIN))
		app.get(Endpoints.TEAMS, {
			if (it.header(header = "Content-Type") == JSON)
				TeamController.apiGetAll(context = it)
			else
				TeamController.webGetAll(context = it)
		}, roles(EVERYONE, DEVELOPER, ADMIN))
		app.post(Endpoints.TEAMS, {
			if (it.header(header = "Content-Type") == JSON)
				TeamController.apiPost(context = it)
		}, roles(DEVELOPER, ADMIN))
		app.get(Endpoints.TEAM, {
			if (it.header(header = "Content-Type") == JSON)
				TeamController.apiGet(context = it)
			else
				TeamController.webGet(context = it)
		}, roles(EVERYONE, DEVELOPER, ADMIN))
		app.get(Endpoints.REFRESH, {
			if (it.status() < 400) {
				refreshData()
				it.status(204)
			}
		}, roles(DEVELOPER, ADMIN))
		app.get(Endpoints.CONFIG, {
			if (it.status() < 400)
				Exceptions.notYetAvailable(context = it)
		}, roles(DEVELOPER, ADMIN))
		app.patch(Endpoints.CONFIG, {
			if (it.status() < 400)
				Exceptions.notYetAvailable(context = it)
		}, roles(ADMIN))
		app.get(Endpoints.HELP, HelpController::get, roles(EVERYONE, DEVELOPER, ADMIN))

		app.error(404) {
			LOGGER.error("${it.path()} >> Host: ${it.host()}, IP: ${it.ip()}, User-Agent: ${it.userAgent()}")
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
		return when (context.header(header = "Access")?.toLowerCase()) {
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