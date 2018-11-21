package macro.neptunes

import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.request.ApplicationReceivePipeline
import io.ktor.request.uri
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import macro.neptunes.core.Config
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

/**
 * Created by Macro303 on 2018-Nov-12.
 */
private val LOGGER = LoggerFactory.getLogger(Application::class.java)

fun main(args: Array<String>) {
	embeddedServer(
		Netty,
		port = Config.port,
		module = Application::neptunes
	).apply { start(wait = true) }
}

fun Application.neptunes() {
	install(ContentNegotiation) {
		gson {
			setPrettyPrinting()
			disableHtmlEscaping()
			serializeNulls()
			generateNonExecutableJson()
		}
	}
	install(DefaultHeaders)
	install(CallLogging){
		level = Level.DEBUG
	}
	install(ConditionalHeaders)
	install(AutoHeadResponse)
	routing {
		intercept(ApplicationCallPipeline.Call){
			LOGGER.info(">> ${call.request.uri}")
		}
		intercept(ApplicationReceivePipeline.Before){
			LOGGER.info("BEFORE >> ${call.request.uri}")
		}
		get("/") {
			call.respondText("Welcome To BIT 269's Neptune's Pride")
		}
	}
}


/*object Application {
	private val LOGGER = LoggerFactory.getLogger(Application::class.java)
	private val GSON = GsonBuilder()
		.serializeNulls()
		.disableHtmlEscaping()
		.create()
	const val JSON = "application/json"
	const val HTML = "text/HTML"

	init {
		if (Config.gameID == null) {
			LOGGER.error("Requires a Game ID")
			exitProcess(0)
		}
		refreshData()
	}

	@JvmStatic
	fun main(args: Array<String>) {
		embeddedServer(Netty, Config.port) {
			routing {
				get("/") {
					call.respondText("Welcome To BIT 269's Neptune's Pride")
				}
			}
		}.start(wait = true)
		val app = Javalin.create().apply {
			enableStaticFiles("/markdown")
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
}*/