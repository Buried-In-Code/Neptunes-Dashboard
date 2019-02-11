package macro.neptunes

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
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
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import macro.neptunes.Server.LOGGER
import macro.neptunes.Server.refreshData
import macro.neptunes.core.Config.Companion.CONFIG
import macro.neptunes.core.Util
import macro.neptunes.core.Util.toJSON
import macro.neptunes.data.GameTable
import macro.neptunes.network.ErrorMessage
import macro.neptunes.network.GameController.gameRoutes
import macro.neptunes.network.HistoryController.historyRoutes
import macro.neptunes.network.PlayerController.parsePlayer
import macro.neptunes.network.PlayerController.playerRoutes
import macro.neptunes.network.SettingsController.settingRoutes
import macro.neptunes.network.TeamController.parseTeam
import macro.neptunes.network.TeamController.teamRoutes
import org.apache.logging.log4j.LogManager
import java.time.Duration
import java.time.LocalDateTime

object Server {
	internal val LOGGER = LogManager.getLogger(this::class.java)
	internal var lastUpdate: LocalDateTime = LocalDateTime.of(1900, 1, 1, 0, 0)

	init {
		LOGGER.info("Initializing Neptune's Pride")
		loggerColours()
		GameTable.insert(CONFIG.gameID)
		refreshData()
	}

	private fun loggerColours() {
		LOGGER.trace("TRACE is Visible")
		LOGGER.debug("DEBUG is Visible")
		LOGGER.info("INFO is Visible")
		LOGGER.warn("WARN is Visible")
		LOGGER.error("ERROR is Visible")
		LOGGER.fatal("FATAL is Visible")
	}

	@JvmStatic
	fun main(args: Array<String>) {
		embeddedServer(
			Netty,
			port = CONFIG.serverPort,
			host = CONFIG.serverAddress,
			module = Application::module
		).apply { start(wait = true) }
	}

	fun refreshData() {
		lastUpdate = LocalDateTime.now()
		LOGGER.info("Last Updated: ${lastUpdate.format(Util.JAVA_FORMATTER)}")
	}
}

fun Application.module() {
	install(ContentNegotiation) {
		gson {
			setPrettyPrinting()
			disableHtmlEscaping()
			serializeNulls()
		}
	}
	install(DefaultHeaders) {
		header(name = HttpHeaders.Server, value = "Ktor-BIT269")
		header(name = "Developer", value = "Macro303")
		header(name = HttpHeaders.ContentLanguage, value = "en-NZ")
	}
	install(Compression)
	install(ConditionalHeaders)
	install(AutoHeadResponse)
	install(FreeMarker) {
		templateLoader = ClassTemplateLoader(this::class.java, "/templates")
	}
	install(StatusPages) {
		exception<Throwable> {
			val error = ErrorMessage(
				code = HttpStatusCode.InternalServerError,
				request = call.request.local.uri,
				message = it.toString(),
				cause = it
			)
			LOGGER.error(error.message, error.cause)
			if (call.request.contentType() == ContentType.Application.Json)
				call.respond(status = error.code, message = error)
			else
				call.respond(
					status = error.code,
					message = FreeMarkerContent(template = "Exception.ftl", model = error)
				)
		}
		status(HttpStatusCode.NotFound) {
			val error = ErrorMessage(
				code = HttpStatusCode.NotFound,
				request = call.request.local.uri,
				message = "Unable to find endpoint"
			)
			if (call.request.contentType() == ContentType.Application.Json)
				call.respond(status = error.code, message = error)
			else
				call.respond(
					status = error.code,
					message = FreeMarkerContent(template = "Exception.ftl", model = error)
				)
		}
	}
	intercept(ApplicationCallPipeline.Setup) {
		LOGGER.debug(">> ${call.request.httpVersion} ${call.request.httpMethod.value} ${call.request.uri}, Content-Type: ${call.request.contentType()}, User-Agent: ${call.request.userAgent()}, Host: ${call.request.host()}:${call.request.port()}")
		val now: LocalDateTime = LocalDateTime.now()
		val difference: Duration = Duration.between(Server.lastUpdate, now)
		if (difference.toMinutes() >= CONFIG.refreshRate) {
			refreshData()
		}
	}
	intercept(ApplicationCallPipeline.Fallback) {
		val statusCode = call.response.status()
		val logMessage =
			"$statusCode << >> ${call.request.httpVersion} ${call.request.httpMethod.value} ${call.request.path()}, Content-Type: ${call.request.contentType()}"
		when (statusCode) {
			null -> LOGGER.error(logMessage)
			HttpStatusCode.NotFound -> LOGGER.error(logMessage)
			HttpStatusCode.NotImplemented -> LOGGER.warn(logMessage)
			else -> LOGGER.info(logMessage)
		}
		LOGGER.debug("${call.response.status()} << ${call.request.path()}, Content-Type: ${call.response.headers["Content-Type"]}")
	}
	install(Routing) {
		route(path = "/api") {
			gameRoutes()
			playerRoutes()
			teamRoutes()
			historyRoutes()
			settingRoutes()
		}
		get(path = "/players/{Alias}") {
			val player = call.parsePlayer(useJson = false) ?: return@get
			call.respond(
				message = FreeMarkerContent(
					template = "Player.ftl",
					model = player.toJSON()
				),
				status = HttpStatusCode.OK
			)
		}
		get(path = "/teams/{Name}") {
			val team = call.parseTeam(useJson = false) ?: return@get
			call.respond(
				message = FreeMarkerContent(
					template = "Team.ftl",
					model = team.toJSON()
				),
				status = HttpStatusCode.OK
			)
		}
		get(path = "/history") {
			call.respond(
				message = FreeMarkerContent(
					template = "History.ftl",
					model = Util.notImplementedMessage(request = call.request)
				), status = HttpStatusCode.NotImplemented
			)
		}
		get(path = "/settings") {
			call.respond(
				message = FreeMarkerContent(
					template = "Exception.ftl",
					model = Util.notImplementedMessage(request = call.request)
				), status = HttpStatusCode.NotImplemented
			)
		}
		get(path = "/about") {
			call.respond(
				message = FreeMarkerContent(
					template = "Exception.ftl",
					model = Util.notImplementedMessage(request = call.request)
				), status = HttpStatusCode.NotImplemented
			)
		}
		static {
			defaultResource(resource = "static/index.html")
			resource(remotePath = "/players", resource = "static/players.html")
			resource(remotePath = "/teams", resource = "static/teams.html")
			resource(remotePath = "/documentation", resource = "static/documentation.html")
			resource(remotePath = "/navbar.html", resource = "static/navbar.html")
			resource(remotePath = "/favicon.ico", resource = "static/images/favicon.ico")
			resource(remotePath = "/background.jpg", resource = "static/images/background.jpg")
			resource(remotePath = "/styles.css", resource = "static/css/styles.css")
			resource(remotePath = "/script.js", resource = "static/js/script.js")
		}
	}
}