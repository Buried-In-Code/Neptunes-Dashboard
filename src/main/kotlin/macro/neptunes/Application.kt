package macro.neptunes

import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import macro.neptunes.core.config.Config
import macro.neptunes.core.Util
import macro.neptunes.core.game.GameController.game
import macro.neptunes.core.game.GameHandler
import macro.neptunes.core.player.PlayerController.players
import macro.neptunes.core.player.PlayerHandler
import macro.neptunes.core.team.TeamController.teams
import macro.neptunes.core.team.TeamHandler
import macro.neptunes.data.HttpBinError
import macro.neptunes.data.WelcomeController.welcome
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.LocalDateTime
import kotlin.system.exitProcess

/**
 * Created by Macro303 on 2018-Nov-12.
 */
object Application {
	private val LOGGER = LoggerFactory.getLogger(Application::class.java)

	init {
		if (Config.gameID == null) {
			LOGGER.error("Requires a Game ID")
			exitProcess(0)
		}
		refreshData()
	}

	@JvmStatic
	fun main(args: Array<String>) {
		val server = embeddedServer(Netty, port = Config.port, host = "0.0.0.0") {
			install(ContentNegotiation) {
				gson {
					setPrettyPrinting()
					disableHtmlEscaping()
					serializeNulls()
					generateNonExecutableJson()
				}
			}
			install(DefaultHeaders) {
				header(name = HttpHeaders.Server, value = "Ktor-BIT-Neptunes")
				header(name = "Developer", value = "Jonah Jackson")
			}
			install(Compression)
			install(ConditionalHeaders)
			install(AutoHeadResponse)
			install(StatusPages) {
				exception<Throwable> {
					val error = HttpBinError(
						code = HttpStatusCode.InternalServerError,
						request = call.request.local.uri,
						message = it.toString(),
						cause = it
					)
					LOGGER.error(error.message, error.cause)
					call.respond(status = error.code, message = error)
				}
				status(HttpStatusCode.NotFound) {
					val error = HttpBinError(
						code = HttpStatusCode.NotFound,
						request = call.request.local.uri,
						message = "Unable to find endpoint"
					)
					LOGGER.error("{}: {}", error.message, error.request, error.cause)
					call.respond(status = error.code, message = error)
				}
			}
			intercept(ApplicationCallPipeline.Setup) {
				LOGGER.debug("SETUP >> {}", call.request.path())
				val now: LocalDateTime = LocalDateTime.now()
				val difference: Duration = Duration.between(Util.lastUpdate, now)
				if (difference.toMinutes() > Config.refreshRate)
					refreshData()
			}
			intercept(ApplicationCallPipeline.Monitoring) {
				LOGGER.debug(
					"MONITORING >> {} {} >> Endpoint: {}, Content-Type: {}, User-Agent: {}, Host: {}:{}",
					call.request.httpVersion,
					call.request.httpMethod.value,
					call.request.uri,
					call.request.contentType(),
					call.request.userAgent(),
					call.request.host(),
					call.request.port()
				)
				LOGGER.info(
					"{} {} >> Endpoint: {}, Content-Type: {}",
					call.request.httpVersion,
					call.request.httpMethod.value,
					call.request.path(),
					call.request.contentType()
				)
			}
			intercept(ApplicationCallPipeline.Features) {
				LOGGER.debug("FEATURES >> {}", call.request.path())
			}
			intercept(ApplicationCallPipeline.Call) {
				LOGGER.debug("CALL >> {}", call.request.path())
			}
			intercept(ApplicationCallPipeline.Fallback) {
				LOGGER.debug("FALLBACK >> {}", call.request.path())
				LOGGER.info("{} << Content-Type: {}", call.response.status(), call.response.headers["Content-Type"])
			}
			routing {
				welcome()
				game()
				players()
				teams()
				static {
					resource("/help", "help.html")
				}
			}
		}
		server.start(wait = true)
	}

	fun refreshData() {
		GameHandler.refreshData()
		PlayerHandler.refreshData()
		if (Config.enableTeams)
			TeamHandler.refreshData()
		else
			TeamHandler.teams = emptyList()
		Util.lastUpdate = LocalDateTime.now()
	}


/*@JvmStatic
	fun main(args: Array<String>) {
		val app = Javalin.create().apply {
			accessManager { handler, context, permittedRoles ->
				val userRole = getUserRole(context = context)
				LOGGER.warn("User access level: $userRole")
				if (permittedRoles.contains(userRole))
					handler.handle(context)
				else
					Exceptions.illegalAccess(context = context)
			}
		}.start()
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
}