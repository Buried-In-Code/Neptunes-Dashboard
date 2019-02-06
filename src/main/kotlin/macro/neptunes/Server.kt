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
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.CachingOptions
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
import macro.neptunes.core.game.GameHandler
import macro.neptunes.core.player.PlayerHandler
import macro.neptunes.core.team.TeamHandler
import macro.neptunes.data.ErrorMessage
import macro.neptunes.data.GameController.gameRoutes
import macro.neptunes.data.PlayerController.playerRoutes
import macro.neptunes.data.SettingsController.settingRoutes
import macro.neptunes.data.TeamController.teamRoutes
import org.apache.logging.log4j.LogManager
import java.time.Duration
import java.time.LocalDateTime

object Server {
	internal val LOGGER = LogManager.getLogger(Server::class.java)
	private var lastUpdate = LocalDateTime.now().minusMinutes((CONFIG.refreshRate + 100).toLong())

	init {
		LOGGER.info("Initializing Neptune's Pride")
		loggerColours()
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
		val now: LocalDateTime = LocalDateTime.now()
		val difference: Duration = Duration.between(lastUpdate, now)
		if (difference.toMinutes() > CONFIG.refreshRate) {
			GameHandler.refreshData()
			PlayerHandler.refreshData()
			TeamHandler.refreshData()
			lastUpdate = LocalDateTime.now()
			LOGGER.info("Last Updated: ${lastUpdate.format(Util.JAVA_FORMATTER)}")
		}
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
				call.respond(FreeMarkerContent(template = "Exception.ftl", model = error))
		}
		status(HttpStatusCode.NotFound) {
			val error = ErrorMessage(
				code = HttpStatusCode.NotFound,
				request = call.request.local.uri,
				message = "Unable to find endpoint"
			)
			LOGGER.error("${error.message}: ${error.request}", error.cause)
			if (call.request.contentType() == ContentType.Application.Json)
				call.respond(status = error.code, message = error)
			else
				call.respond(FreeMarkerContent(template = "Exception.ftl", model = error))
		}
	}
	intercept(ApplicationCallPipeline.Setup) {
		LOGGER.debug(">> ${call.request.httpVersion} ${call.request.httpMethod.value} ${call.request.uri}, Content-Type: ${call.request.contentType()}, User-Agent: ${call.request.userAgent()}, Host: ${call.request.host()}:${call.request.port()}")
		refreshData()
	}
	intercept(ApplicationCallPipeline.Fallback) {
		LOGGER.info("${call.response.status()} << >> ${call.request.httpVersion} ${call.request.httpMethod.value} ${call.request.path()}, Content-Type: ${call.request.contentType()}")
		LOGGER.debug("${call.response.status()} << ${call.request.path()}, Content-Type: ${call.response.headers["Content-Type"]}")
	}
	install(Routing) {
		route(path = "/api") {
			gameRoutes()
			playerRoutes()
			teamRoutes()
			settingRoutes()
		}
		route(path = "/players/{Alias}") {
			get {
				val alias = call.parameters["Alias"]
				val player = PlayerHandler.players.sorted().firstOrNull { it.alias.equals(alias, ignoreCase = true) }
				if (alias == null || player == null)
					call.respond(
						message = FreeMarkerContent(
							template = "Message.ftl",
							model = Util.notFoundMessage(type = "Player", field = "Alias", value = alias)
						),
						status = HttpStatusCode.NotFound
					)
				else
					call.respond(
						message = FreeMarkerContent(
							template = "Player.ftl",
							model = player.toJson()
						),
						status = HttpStatusCode.OK
					)
			}
		}
		route(path = "/teams/{Name}") {
			get {
				val name = call.parameters["Name"]
				val team = TeamHandler.teams.sorted().firstOrNull { it.name.equals(name, ignoreCase = true) }
				if (name == null || team == null)
					call.respond(
						message = FreeMarkerContent(
							template = "Message.ftl",
							model = Util.notFoundMessage(type = "Team", field = "Name", value = name)
						),
						status = HttpStatusCode.NotFound
					)
				else
					call.respond(
						message = FreeMarkerContent(
							template = "Team.ftl",
							model = team.toJson()
						),
						status = HttpStatusCode.OK
					)
			}
		}
		get(path = "/settings") {
			call.respond(
				message = FreeMarkerContent(
					template = "Message.ftl",
					model = Util.notImplementedMessage(request = call.request)
				), status = HttpStatusCode.NotImplemented
			)
		}
		get(path = "/about") {
			call.respond(
				message = FreeMarkerContent(
					template = "Message.ftl",
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