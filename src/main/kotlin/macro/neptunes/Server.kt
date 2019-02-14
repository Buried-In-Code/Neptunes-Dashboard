package macro.neptunes

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
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
import macro.neptunes.backend.Neptunes
import macro.neptunes.config.Config.Companion.CONFIG
import macro.neptunes.config.ConfigRouter.settingRoutes
import macro.neptunes.game.GameRouter.gameRoutes
import macro.neptunes.game.GameTable
import macro.neptunes.history.HistoryRouter.historyRoutes
import macro.neptunes.player.PlayerRouter
import macro.neptunes.player.PlayerRouter.playerRoutes
import macro.neptunes.team.TeamRouter
import macro.neptunes.team.TeamRouter.teamRoutes
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import java.time.Duration
import java.time.LocalDateTime

object Server {
	internal val LOGGER = LogManager.getLogger(this::class.java)

	init {
		LOGGER.info("Initializing Neptune's Pride")
		loggerColours()
		Neptunes.updateGame()
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
	install(XForwardedHeaderSupport)
	install(FreeMarker) {
		templateLoader = ClassTemplateLoader(this::class.java, "/templates")
	}
	install(StatusPages) {
		exception<Throwable> {
			val error = ErrorMessage(
				code = HttpStatusCode.InternalServerError,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = it.toString(),
				cause = it
			)
			call.respond(error = error, logLevel = Level.FATAL)
		}
		exception<InvalidContentTypeException> {
			val error = ErrorMessage(
				code = HttpStatusCode.BadRequest,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = it.toString()
			)
			call.respond(error = error)
		}
		exception<InvalidBodyException> {
			val error = ErrorMessage(
				code = HttpStatusCode.BadRequest,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = it.toString()
			)
			call.respond(error = error, logLevel = Level.WARN)
		}
		exception<DataExistsException> {
			val error = ErrorMessage(
				code = HttpStatusCode.Conflict,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = it.toString()
			)
			call.respond(error = error)
		}
		exception<DataNotFoundException> {
			val error = ErrorMessage(
				code = HttpStatusCode.NotFound,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = it.toString()
			)
			call.respond(error = error)
		}
		exception<NotImplementedException> {
			val error = ErrorMessage(
				code = HttpStatusCode.NotImplemented,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = "This endpoint hasn't been implemented yet, feel free to make a pull request and add it."
			)
			call.respond(error = error, logLevel = Level.WARN)
		}
		status(HttpStatusCode.NotFound) {
			val error = ErrorMessage(
				code = HttpStatusCode.NotFound,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = "Unable to find endpoint"
			)
			call.respond(error = error, logLevel = Level.WARN)
		}
	}
	intercept(ApplicationCallPipeline.Setup) {
		val now: LocalDateTime = LocalDateTime.now()
		val difference: Duration = Duration.between(GameTable.select()?.lastUpdated, now)
		if (difference.toMinutes() >= CONFIG.refreshRate)
			Neptunes.updateGame()
	}
	intercept(ApplicationCallPipeline.Monitoring) {
		LOGGER.debug(">> ${call.request.httpVersion} ${call.request.httpMethod.value} ${call.request.uri}, Content-Type: ${call.request.contentType()}, User-Agent: ${call.request.userAgent()}, Host: ${call.request.origin.remoteHost}:${call.request.port()}")
	}
	intercept(ApplicationCallPipeline.Fallback) {
		val statusCode = call.response.status() ?: HttpStatusCode.NotFound
		val logMessage = "$statusCode: ${call.request.httpMethod.value} - ${call.request.path()}"
		when (statusCode) {
			HttpStatusCode.InternalServerError -> LOGGER.fatal(logMessage)
			HttpStatusCode.NotFound -> LOGGER.error(logMessage)
			HttpStatusCode.Conflict -> LOGGER.error(logMessage)
			HttpStatusCode.BadRequest -> LOGGER.error(logMessage)
			HttpStatusCode.NotImplemented -> LOGGER.warn(logMessage)
			else -> LOGGER.info(logMessage)
		}
	}
	install(Routing) {
		route(path = "/api") {
			intercept(ApplicationCallPipeline.Features) {
				if (!call.request.contentType().match(ContentType.Application.Json))
					throw InvalidContentTypeException(value = call.request.contentType())
			}
			gameRoutes()
			playerRoutes()
			teamRoutes()
			historyRoutes()
			settingRoutes()
		}
		get(path = "/players/{Alias}") {
			val player = PlayerRouter.get(call = call)
			call.respond(
				message = FreeMarkerContent(
					template = "player.ftl",
					model = player.toOutput()
				),
				status = HttpStatusCode.OK
			)
		}
		get(path = "/teams/{Name}") {
			val team = TeamRouter.get(call = call)
			call.respond(
				message = FreeMarkerContent(
					template = "team.ftl",
					model = team.toOutput()
				),
				status = HttpStatusCode.OK
			)
		}
		get(path = "/history") {
			throw NotImplementedException()
		}
		get(path = "/settings") {
			throw NotImplementedException()
		}
		get(path = "/about") {
			throw NotImplementedException()
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

suspend fun ApplicationCall.respond(error: ErrorMessage, logLevel: Level = Level.ERROR) {
	if (request.local.uri.startsWith("/api") || request.contentType() == ContentType.Application.Json)
		respond(message = error, status = error.code)
	else
		respond(
			message = FreeMarkerContent(template = "Exception.ftl", model = error),
			status = error.code
		)
	if (error.code != HttpStatusCode.NotFound)
		when (logLevel) {
			Level.WARN -> LOGGER.warn("${error.code}: ${request.httpMethod.value} - ${request.path()}")
			Level.ERROR -> LOGGER.error("${error.code}: ${request.httpMethod.value} - ${request.path()}")
			Level.FATAL -> LOGGER.fatal(error.message)
		}
}