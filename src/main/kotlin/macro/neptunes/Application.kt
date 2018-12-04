package macro.neptunes

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.defaultResource
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import macro.neptunes.core.Config.Companion.CONFIG
import macro.neptunes.core.Util
import macro.neptunes.core.game.GameHandler
import macro.neptunes.core.player.PlayerHandler
import macro.neptunes.core.team.TeamHandler
import macro.neptunes.data.HttpBinError
import macro.neptunes.data.controllers.GameController.game
import macro.neptunes.data.controllers.PlayerController.players
import macro.neptunes.data.controllers.TeamController.teams
import macro.neptunes.data.controllers.UtilController.util
import macro.neptunes.data.controllers.WelcomeController.welcome
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
		LOGGER.info("Initializing Neptune's Pride")
		loggerColours()
		if (CONFIG.gameID == null) {
			LOGGER.error("Requires a Game ID")
			exitProcess(0)
		}
		refreshData()
	}

	private fun loggerColours() {
		LOGGER.trace("Trace is Visible")
		LOGGER.debug("Debug is Visible")
		LOGGER.info("Info is Visible")
		LOGGER.warn("Warn is Visible")
		LOGGER.error("Error is Visible")
	}

	@JvmStatic
	fun main(args: Array<String>) {
		val server = embeddedServer(Netty, port = CONFIG.port, host = "0.0.0.0") {
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
			install(FreeMarker) {
				templateLoader = ClassTemplateLoader(Application::class.java, "/templates")
			}
			install(StatusPages) {
				exception<Throwable> {
					val error = HttpBinError(
						code = HttpStatusCode.InternalServerError,
						request = call.request.local.uri,
						message = it.toString(),
						cause = it
					)
					LOGGER.error(error.message, error.cause)
					if (call.request.contentType() == ContentType.Application.Json)
						call.respond(status = error.code, message = error)
					else
						call.respond(FreeMarkerContent(template = "error.ftl", model = mapOf("error" to error)))
				}
				status(HttpStatusCode.NotFound) {
					val error = HttpBinError(
						code = HttpStatusCode.NotFound,
						request = call.request.local.uri,
						message = "Unable to find endpoint"
					)
					LOGGER.error("{}: {}", error.message, error.request, error.cause)
					if (call.request.contentType() == ContentType.Application.Json)
						call.respond(status = error.code, message = error)
					else
						call.respond(FreeMarkerContent(template = "error.ftl", model = mapOf("error" to error)))
				}
				status(HttpStatusCode.NotImplemented) {
					val error = HttpBinError(
						code = HttpStatusCode.NotImplemented,
						request = call.request.local.uri,
						message = "Not Yet Implemented"
					)
					LOGGER.error("{}: {}", error.message, error.request, error.cause)
					if (call.request.contentType() == ContentType.Application.Json)
						call.respond(status = error.code, message = error)
					else
						call.respond(FreeMarkerContent(template = "error.ftl", model = mapOf("error" to error)))
				}
			}
			intercept(ApplicationCallPipeline.Setup) {
				LOGGER.debug("SETUP >> {}", call.request.path())
				val now: LocalDateTime = LocalDateTime.now()
				val difference: Duration = Duration.between(Util.lastUpdate, now)
				if (difference.toMinutes() > CONFIG.refreshRate)
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
				util()
				static {
					defaultResource(resource = "static/index.html")
					resource(remotePath = "/help", resource = "static/help.html")
					resource(remotePath = "/favicon.ico", resource = "static/images/favicon.ico")
					resource(remotePath = "/background.jpg", resource = "static/images/background.jpg")
					resource(remotePath = "/styles.css", resource = "static/css/styles.css")
				}
			}
		}
		server.start(wait = true)
	}

	fun refreshData() {
		GameHandler.refreshData()
		PlayerHandler.refreshData()
		if (CONFIG.enableTeams)
			TeamHandler.refreshData()
		else
			TeamHandler.teams = emptyList()
		Util.lastUpdate = LocalDateTime.now()
	}
}