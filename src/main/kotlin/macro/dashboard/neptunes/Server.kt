package macro.dashboard.neptunes

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.defaultResource
import io.ktor.http.content.resource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import macro.dashboard.neptunes.Server.LOGGER
import macro.dashboard.neptunes.config.Config.Companion.CONFIG
import macro.dashboard.neptunes.config.ConfigRouter.settingRoutes
import macro.dashboard.neptunes.game.GameController.gameRoutes
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.PlayerController
import macro.dashboard.neptunes.player.PlayerController.playerRoutes
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.player.PlayerTurnTable
import macro.dashboard.neptunes.team.TeamController
import macro.dashboard.neptunes.team.TeamController.teamRoutes
import macro.dashboard.neptunes.team.TeamTable
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.exists

object Server {
	internal val LOGGER = LogManager.getLogger(this::class.java)

	init {
		LOGGER.info("Initializing Neptune's Dashboard")
		loggerColours()
		checkDatabase()
	}

	private fun loggerColours() {
		LOGGER.trace("TRACE is Visible")
		LOGGER.debug("DEBUG is Visible")
		LOGGER.info("INFO is Visible")
		LOGGER.warn("WARN is Visible")
		LOGGER.error("ERROR is Visible")
		LOGGER.fatal("FATAL is Visible")
	}

	private fun checkDatabase() {
		Util.query {
			GameTable.exists()
			TeamTable.exists()
			PlayerTable.exists()
			PlayerTurnTable.exists()
		}
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
				message = it.message ?: "",
				cause = it
			)
			call.respond(error = error, logLevel = Level.FATAL)
		}
		exception<BadRequestException> {
			val error = ErrorMessage(
				code = HttpStatusCode.BadRequest,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = it.message
			)
			call.respond(error = error, logLevel = Level.WARN)
		}
		exception<UnauthorizedException>{
			val error = ErrorMessage(
				code = HttpStatusCode.Unauthorized,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = it.message
			)
			call.respond(error = error)
		}
		exception<NotFoundException>{
			val error = ErrorMessage(
				code = HttpStatusCode.NotFound,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = it.message
			)
			call.respond(error = error, logLevel = Level.WARN)
		}
		exception<ConflictException>{
			val error = ErrorMessage(
				code = HttpStatusCode.Conflict,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = it.message
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
	intercept(ApplicationCallPipeline.Monitoring) {
		LOGGER.debug(">> ${call.request.httpVersion} ${call.request.httpMethod.value} ${call.request.uri}, Content-Type: ${call.request.contentType()}, User-Agent: ${call.request.userAgent()}, Host: ${call.request.origin.remoteHost}:${call.request.port()}")
	}
	intercept(ApplicationCallPipeline.Fallback) {
		val statusCode = call.response.status() ?: HttpStatusCode.NotFound
		val logMessage = "$statusCode: ${call.request.httpMethod.value} - ${call.request.uri}"
		when (statusCode) {
			HttpStatusCode.InternalServerError -> LOGGER.fatal(logMessage)
			HttpStatusCode.NotFound -> LOGGER.warn(logMessage)
			HttpStatusCode.Conflict -> LOGGER.warn(logMessage)
			HttpStatusCode.Unauthorized -> LOGGER.error(logMessage)
			HttpStatusCode.BadRequest -> LOGGER.warn(logMessage)
			HttpStatusCode.NotImplemented -> LOGGER.warn(logMessage)
			else -> LOGGER.info(logMessage)
		}
	}
	install(Routing) {
		route(path = "/api") {
			intercept(ApplicationCallPipeline.Features) {
				if (!call.request.contentType().match(ContentType.Application.Json))
					throw BadRequestException(message = "The API requires the use of the header 'Content-Type' as 'application/json'")
				val authorization = call.request.header("Authorization")
				if (call.request.httpMethod != HttpMethod.Get && authorization != "Basic VXNlcm5hbWU6UGFzc3dvcmQ=")
					throw UnauthorizedException(message = "You are not Authorized to use this endpoint")
			}
			gameRoutes()
			playerRoutes()
			teamRoutes()
			settingRoutes()
		}
		get(path = "/players/{ID}") {
			val player = PlayerController.get(call = call)
			call.respond(
				message = FreeMarkerContent(
					template = "Player.ftl",
					model = player.toOutput(showGame = true, showTeam = true, showTurns = true)
				),
				status = HttpStatusCode.OK
			)
		}
		get(path = "/teams/{ID}") {
			val team = TeamController.get(call = call)
			call.respond(
				message = FreeMarkerContent(
					template = "Team.ftl",
					model = team.toOutput(showGame = true, showPlayers = true)
				),
				status = HttpStatusCode.OK
			)
		}
		get(path = "/settings") {
			throw NotImplementedException()
		}
		static {
			resources(resourcePackage = "static/images")
			resources(resourcePackage = "static/css")
			resources(resourcePackage = "static/js")
			defaultResource(resource = "static/index.html")
			resource(remotePath = "/navbar.html", resource = "static/navbar.html")
			resource(remotePath = "/players", resource = "static/players.html")
			resource(remotePath = "/teams", resource = "static/teams.html")
			resource(remotePath = "/documentation", resource = "static/documentation.html")
			resource(remotePath = "/about", resource = "static/about.html")
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
			Level.FATAL -> LOGGER.fatal("${error.code}: ${request.httpMethod.value} - ${request.path()} - ${error.message}")
		}
}