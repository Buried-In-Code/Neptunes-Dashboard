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
import io.ktor.routing.Routing
import io.ktor.routing.accept
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import macro.dashboard.neptunes.Config.Companion.CONFIG
import macro.dashboard.neptunes.backend.Proteus
import macro.dashboard.neptunes.game.GameController.gameRoutes
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.Player
import macro.dashboard.neptunes.player.PlayerController.playerRoutes
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.player.TechnologyTable
import macro.dashboard.neptunes.player.TurnTable
import macro.dashboard.neptunes.team.Team
import macro.dashboard.neptunes.team.TeamController.teamRoutes
import macro.dashboard.neptunes.team.TeamTable
import org.jetbrains.exposed.sql.exists
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

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
			TurnTable.exists()
			TeamTable.exists()
			TechnologyTable.exists()
		}
	}

	private fun checkConfigGames() {
		val game = GameTable.select(ID = CONFIG.gameID)
		if (game == null) {
			when(Proteus.getGame(gameID = CONFIG.gameID, code = CONFIG.gameCode)){
				true -> LOGGER.info("New Game Loaded => ${CONFIG.gameID}")
				false -> LOGGER.error("Invalid Game Type => ${CONFIG.gameID}")
				else -> LOGGER.error("Failed Game Load: ${CONFIG.gameID} => ${CONFIG.gameCode}")
			}
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
	install(CallLogging) {
		level = Level.INFO
	}
	install(ContentNegotiation) {
		gson {
			setPrettyPrinting()
			disableHtmlEscaping()
			serializeNulls()
		}
	}
	install(DefaultHeaders) {
		header(name = HttpHeaders.Server, value = "Neptunes-Dashboard")
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
			call.respond(error = error, logLevel = Level.ERROR)
		}
		exception<BadRequestException> {
			val error = ErrorMessage(
				code = HttpStatusCode.BadRequest,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = it.message
			)
			call.respond(error = error, logLevel = Level.WARN)
		}
		exception<UnauthorizedException> {
			val error = ErrorMessage(
				code = HttpStatusCode.Unauthorized,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = it.message
			)
			call.respond(error = error, logLevel = Level.ERROR)
		}
		exception<ConflictException> {
			val error = ErrorMessage(
				code = HttpStatusCode.Conflict,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = it.message
			)
			call.respond(error = error, logLevel = Level.WARN)
		}
		exception<NotImplementedException> {
			val error = ErrorMessage(
				code = HttpStatusCode.NotImplemented,
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
		status(HttpStatusCode.NotAcceptable) {
			val error = ErrorMessage(
				code = HttpStatusCode.NotAcceptable,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = "Invalid Accept Header"
			)
			call.respond(error = error, logLevel = Level.WARN)
		}
	}
	intercept(ApplicationCallPipeline.Monitoring) {
		application.log.debug(">> ${call.request.httpVersion} ${call.request.httpMethod.value} ${call.request.uri}, Accept: ${call.request.accept()}, Content-Type: ${call.request.contentType()}, User-Agent: ${call.request.userAgent()}, Host: ${call.request.origin.remoteHost}:${call.request.port()}")
	}
	install(Routing) {
		trace { application.log.trace(it.buildText()) }
		route(path = "/api") {
			accept(ContentType.Application.Json) {
				gameRoutes()
				playerRoutes()
				teamRoutes()
			}
		}
		route(path = "/players/{alias}") {
			fun ApplicationCall.getPlayer(): Player {
				val alias = parameters["alias"]
				return PlayerTable.select(alias = alias ?: "%")
					?: throw NotFoundException(message = "No Player was found with the given alias '$alias'")
			}
			get {
				call.respond(
					message = FreeMarkerContent(
						template = "Player.ftl",
						model = call.getPlayer().toOutput(showGame = true, showTeam = true, showTurns = true)
					),
					status = HttpStatusCode.OK
				)
			}
		}
		route(path = "/teams/{name}") {
			fun ApplicationCall.getTeam(): Team {
				val name = parameters["name"]
				return TeamTable.select(name = name ?: "%")
					?: throw NotFoundException(message = "No Team was found with the given name '$name'")
			}
			get {
				call.respond(
					message = FreeMarkerContent(
						template = "Team.ftl",
						model = call.getTeam().toOutput(showGame = true, showPlayers = true)
					),
					status = HttpStatusCode.OK
				)
			}
		}
		static {
			resources(resourcePackage = "static/images")
			resources(resourcePackage = "static/css")
			resources(resourcePackage = "static/js")
			defaultResource(resource = "/static/index.html")
			resource(remotePath = "/navbar.html", resource = "static/navbar.html")
			resource(remotePath = "/players", resource = "static/players.html")
			resource(remotePath = "/teams", resource = "static/teams.html")
			resource(remotePath = "/documentation.yaml", resource = "static/Neptunes-Dashboard.yaml")
			resource(remotePath = "/about", resource = "static/about.html")
		}
	}
}

suspend fun ApplicationCall.respond(error: ErrorMessage, logLevel: Level) {
	if (request.local.uri.startsWith(prefix = "/api") || request.accept()?.contains(ContentType.Application.Json.toString()) == true)
		respond(message = error, status = error.code)
	else
		respond(
			message = FreeMarkerContent(template = "Exception.ftl", model = error),
			status = error.code
		)
	when (logLevel) {
		Level.WARN -> application.log.warn("${error.code}: ${request.httpMethod.value} - ${request.uri} - ${error.message}")
		Level.ERROR -> application.log.error("${error.code}: ${request.httpMethod.value} - ${request.uri} - ${error.message}")
		else -> application.log.info("${error.code}: ${request.httpMethod.value} - ${request.uri}")
	}
}