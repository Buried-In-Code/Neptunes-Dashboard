package macro.dashboard.neptunes

import io.javalin.Javalin
import io.javalin.core.security.Role
import io.javalin.core.security.SecurityUtil.roles
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.UnauthorizedResponse
import io.javalin.plugin.json.FromJsonMapper
import io.javalin.plugin.json.JavalinJson
import io.javalin.plugin.json.ToJsonMapper
import macro.dashboard.neptunes.Config.Companion.CONFIG
import macro.dashboard.neptunes.Server.SecurityRole.*
import macro.dashboard.neptunes.backend.Proteus
import macro.dashboard.neptunes.cycle.CycleHandler
import macro.dashboard.neptunes.game.GameHandler
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.cycle.CycleTable
import macro.dashboard.neptunes.player.PlayerHandler
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.team.TeamTable
import org.jetbrains.exposed.sql.exists
import org.slf4j.LoggerFactory

object Server {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	init {
		LOGGER.info("Initializing Neptune's Dashboard")
		loggerColours()
		checkDatabase()
		checkConfigGames()
	}

	private fun loggerColours() {
		LOGGER.trace("TRACE is Visible")
		LOGGER.debug("DEBUG is Visible")
		LOGGER.info("INFO is Visible")
		LOGGER.warn("WARN is Visible")
		LOGGER.error("ERROR is Visible")
	}

	private fun checkDatabase() {
		Util.query(description = "Check All Tables Exist") {
			GameTable.exists()
			PlayerTable.exists()
			CycleTable.exists()
			TeamTable.exists()
		}
	}

	private fun checkConfigGames() {
		val game = GameTable.select(ID = CONFIG.gameID)
		if (game == null) {
			when (Proteus.getGame(gameID = CONFIG.gameID, code = CONFIG.gameCode)) {
				true -> LOGGER.info("New Game Loaded => ${CONFIG.gameID}")
				false -> LOGGER.error("Invalid Game Type => ${CONFIG.gameID}")
				else -> LOGGER.error("Failed Game Load: ${CONFIG.gameID} => ${CONFIG.gameCode}")
			}
		}
	}

	@JvmStatic
	fun main(args: Array<String>) {
		JavalinJson.fromJsonMapper = object : FromJsonMapper {
			override fun <T> map(json: String, targetClass: Class<T>): T {
				return Util.GSON.fromJson(json, targetClass)
			}
		}
		JavalinJson.toJsonMapper = object : ToJsonMapper {
			override fun map(obj: Any): String {
				return Util.GSON.toJson(obj)
			}
		}
		Javalin.create {
			it.showJavalinBanner = false
			it.requestLogger { ctx, timeMs ->
				LOGGER.info("${ctx.protocol()} ${ctx.method()} ${ctx.path()} took $timeMs ms")
				LOGGER.debug("${ctx.fullUrl()} >> Accept: ${ctx.header("Accept")}, Host: ${ctx.host()}, IP: ${ctx.ip()}")
			}
			it.accessManager { handler, ctx, roles ->
				val userRole = getRole(ctx = ctx)
				LOGGER.debug("User Access Level: $userRole")
				if (roles.contains(userRole))
					handler.handle(ctx)
				else
					throw UnauthorizedResponse("Invalid Access Level")
			}
			it.defaultContentType = "application/json"
		}.start(CONFIG.serverPort).apply {
			exception(Exception::class.java) { e, _ -> LOGGER.error("Exception Occured", e) }
			get("/api/game", GameHandler::getGame, roles(EVERYONE, DEVELOPER, ADMIN))
			put("/api/game", GameHandler::updateGame, roles(DEVELOPER, ADMIN))
			get("/api/players", PlayerHandler::getPlayers, roles(EVERYONE, DEVELOPER, ADMIN))
			get("/api/players/:player-id", PlayerHandler::getPlayer, roles(EVERYONE, DEVELOPER, ADMIN))
			put("/api/players/:player-id", PlayerHandler::updatePlayer, roles(DEVELOPER, ADMIN))
			get("/api/players/:player-id/cycles", CycleHandler::getCycles, roles(EVERYONE, DEVELOPER, ADMIN))
			get("/api/players/:player-id/cycles/:cycle", CycleHandler::getCycle, roles(EVERYONE, DEVELOPER, ADMIN))
			get("/api/teams", { ctx ->
				ctx.json(TeamTable.search().map { it.toOutput(showGame = false, showPlayers = false) })
			}, roles(EVERYONE, DEVELOPER, ADMIN))
			get("/api/teams/:team-id", { ctx ->
				val teamID = ctx.pathParam("team-id").toIntOrNull() ?: throw BadRequestResponse()
				ctx.json(TeamTable.select(ID = teamID)?: emptyMap<String, Any?>())
			}, roles(EVERYONE, DEVELOPER, ADMIN))
			post("/api/teams", { ctx ->
				ctx.status(201)
			}, roles(DEVELOPER, ADMIN))
			put("/api/teams/:team-id", { ctx ->
				val teamID = ctx.pathParam("team-id").toIntOrNull() ?: throw BadRequestResponse()
				ctx.status(204)
			}, roles(DEVELOPER, ADMIN))
		}
	}

	private fun getRole(ctx: Context): Role {
		return EVERYONE
	}

	internal enum class SecurityRole : Role {
		EVERYONE,
		DEVELOPER,
		ADMIN
	}
}