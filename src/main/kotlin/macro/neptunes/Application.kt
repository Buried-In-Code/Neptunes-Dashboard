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
import macro.neptunes.core.Config
import macro.neptunes.core.Util
import macro.neptunes.core.game.GameHandler
import macro.neptunes.core.player.PlayerHandler
import macro.neptunes.core.team.TeamHandler
import macro.neptunes.data.APIEndpoints
import macro.neptunes.data.APIRoles.*
import macro.neptunes.data.ContentType.JSON
import macro.neptunes.data.ContentType.HTML
import macro.neptunes.data.Exceptions
import macro.neptunes.data.WebEndpoints
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
				if (permittedRoles.contains(userRole))
					handler.handle(context)
				else
					context.status(401).json(mapOf(Pair("Error", "'${context.path()}' isn't for you")))
				LOGGER.warn("User access level: $userRole")
			}
		}.start()

		app.before("/api*") {
			if (it.header("Content-Type") != JSON.value) {
				Exceptions.contentType(context = it)
			}
		}
		app.before {
			when (it.method()) {
				"HEAD" -> LOGGER.info("${it.protocol()} ${it.method()} >> Endpoint: ${it.path()}, Content-Type: ${it.header("Content-Type")}")
				"GET" -> LOGGER.info("${it.protocol()} ${it.method()} >> Endpoint: ${it.path()}, Content-Type: ${it.header("Content-Type")}")
				"POST" -> LOGGER.info("${it.protocol()} ${it.method()} >> Endpoint: ${it.path()}, Content-Type: ${it.header("Content-Type")}, Body: ${it.body()}")
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
			path("/web") {
				get("/", {
					it.html("<html><h1>Welcome to BIT 269's Neptune's Pride</h1></html>")
				}, roles(EVERYONE, DEVELOPER, ADMIN))
				get("/game", {
					WebEndpoints.Game.get(context = it)
				}, roles(EVERYONE, DEVELOPER, ADMIN))
				get("/players", {
					WebEndpoints.Player.getAll(context = it)
				}, roles(EVERYONE, DEVELOPER, ADMIN))
				get("/player", {
					WebEndpoints.Player.get(context = it)
				}, roles(EVERYONE, DEVELOPER, ADMIN))
				get("/leaderboard", {
					WebEndpoints.Leaderboard.get(context = it)
				}, roles(EVERYONE, DEVELOPER, ADMIN))
				get("/help", {
					it.html(Util.htmlToString(location = "help"))
				}, roles(EVERYONE, DEVELOPER, ADMIN))
			}
		}

		app.routes {
			path("/api") {
				get("/", {
					if (it.status() < 400)
						it.json(mapOf(Pair("Message", "Welcome to BIT 269's Neptune's Pride API")))
				}, roles(DEVELOPER, ADMIN))
				get("/game", {
					if (it.status() < 400)
						APIEndpoints.Game.get(context = it)
				}, roles(DEVELOPER, ADMIN))
				get("/players", {
					if (it.status() < 400)
						APIEndpoints.Player.getAll(context = it)
				}, roles(DEVELOPER, ADMIN))
				get("/player", {
					if (it.status() < 400)
						APIEndpoints.Player.get(context = it)
				}, roles(DEVELOPER, ADMIN))
				post("/player", {
					if (it.status() < 400)
						APIEndpoints.Player.add(context = it)
				}, roles(DEVELOPER, ADMIN))
				get("/leaderboard", {
					if (it.status() < 400)
						APIEndpoints.Leaderboard.get(context = it)
				}, roles(DEVELOPER, ADMIN))
				get("/refresh", {
					if (it.status() < 400) {
						refreshData()
						it.status(204)
					}
				}, roles(DEVELOPER, ADMIN))
				post("/config", {
					if (it.status() < 400)
						Exceptions.notYetAvailable(context = it)
				}, roles(ADMIN))
				get("/help", {
					if (it.status() < 400)
						APIEndpoints.Help.get(context = it)
				}, roles(DEVELOPER, ADMIN))
			}
		}
	}

	private fun refreshData() {
		Util.game = GameHandler.getData()
		Util.players = PlayerHandler.getData()
		if (Config.enableTeams)
			Util.teams = TeamHandler.getData(players = Util.players)
		else
			Util.teams = null
		Util.lastUpdate = LocalDateTime.now()
	}

	private fun getUserRole(context: Context): Role {
		return when (context.header("Access")?.toLowerCase()) {
			"admin" -> ADMIN
			"dev" -> DEVELOPER
			else -> EVERYONE
		}
	}
}