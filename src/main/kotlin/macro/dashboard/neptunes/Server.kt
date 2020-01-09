package macro.dashboard.neptunes

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.FreeMarker
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.http.withCharset
import io.ktor.jackson.jackson
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import macro.dashboard.neptunes.config.Config
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.player.Player
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.team.Team
import macro.dashboard.neptunes.team.TeamTable
import macro.dashboard.neptunes.tick.Tick
import macro.dashboard.neptunes.tick.TickTable
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object Server {
	private val LOGGER = LogManager.getLogger(Server::class.java)

	init {
		LOGGER.info("Initializing Neptune's Dashboard")
		checkLogLevels()
	}

	private fun checkLogLevels() {
		LOGGER.trace("TRACE | is Visible")
		LOGGER.debug("DEBUG | is Visible")
		LOGGER.info("INFO  | is Visible")
		LOGGER.warn("WARN  | is Visible")
		LOGGER.error("ERROR | is Visible")
	}

	@KtorExperimentalAPI
	@JvmStatic
	fun main(args: Array<String>) {
		embeddedServer(
			Netty,
			port = Config.INSTANCE.server.port ?: 5505,
			host = Config.INSTANCE.server.hostName ?: "localhost",
			module = Application::module
		).apply { start(wait = true) }
	}
}

@KtorExperimentalAPI
fun Application.module() {
	install(ContentNegotiation) {
		jackson {
			configure(SerializationFeature.INDENT_OUTPUT, true)
			setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
				indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
				indentObjectsWith(DefaultIndenter("  ", "\n"))
			})
			registerModule(JavaTimeModule())
		}
	}
	install(DefaultHeaders) {
		header(name = "Developer", value = "Macro303")
		header(name = HttpHeaders.Server, value = "Neptunes-Dashboard")
		header(name = HttpHeaders.AcceptLanguage, value = "en-NZ")
		header(name = HttpHeaders.ContentLanguage, value = "en-NZ")
	}
	install(ConditionalHeaders)
	install(AutoHeadResponse)
	install(FreeMarker) {
		templateLoader = ClassTemplateLoader(this::class.java, "/templates")
	}
	install(StatusPages) {
		exception<Throwable> {
			application.log.error(it.localizedMessage)
			call.respond(
				TextContent(
					"Message: ${it.localizedMessage}",
					ContentType.Text.Plain.withCharset(Charsets.UTF_8)
				)
			)
		}
		status(HttpStatusCode.NotFound) {
			call.respond(
				TextContent(
					"${it.value} ${it.description}",
					ContentType.Text.Plain.withCharset(Charsets.UTF_8),
					it
				)
			)
		}
	}
	install(Routing) {
		intercept(ApplicationCallPipeline.Setup) {
			application.log.debug("==> ${call.request.httpVersion} ${call.request.httpMethod.value} ${call.request.uri}, Accept: ${call.request.accept()}, Content-Type: ${call.request.contentType()}, User-Agent: ${call.request.userAgent()}")
		}
		trace {
			application.log.trace(it.buildText())
		}
		route(path = "/api") {
			route(path = "/games") {
				get {
					newSuspendedTransaction(db = Util.database) {
						call.respond(Game.all().sorted().map {
							it.toJson(full = true)
						})
					}
				}
				route(path = "/{gameID}") {
					fun ApplicationCall.getGame(): Game {
						val gameId = parameters["gameID"]?.toLongOrNull()
							?: throw BadRequestException("Invalid Game ID")
						return Game.findById(gameId) ?: throw NotFoundException("Unable to find Game")
					}

					get {
						newSuspendedTransaction(db = Util.database) {
							call.respond(call.getGame().toJson(full = true))
						}
					}
					post {
						val request = call.receiveOrNull<NewGameRequest>()
							?: throw BadRequestException("Invalid New Game Request")
						val newId = request.gameID ?: throw BadRequestException("Invalid Game ID")
						val newCode = request.code ?: throw BadRequestException("Invalid Game Code")
						newSuspendedTransaction(db = Util.database) {
							var game = Game.findById(newId)
							if (game != null) {
								call.respond(
									message = "Game with ID already Exists",
									status = HttpStatusCode.Conflict
								)
							} else {
								Triton.getGame(gameId = newId, code = newCode)
								game = Game.findById(newId) ?: throw NotFoundException("Unable to find Game")
								call.respond(
									message = game.toJson(full = true),
									status = HttpStatusCode.Created
								)
							}
						}
					}
					route(path = "/players") {
						get {
							newSuspendedTransaction(db = Util.database) {
								call.respond(call.getGame().players.map { it.toJson(full = true) })
							}
						}
						route(path = "/{playerID}") {
							fun ApplicationCall.getPlayer(): Player {
								val gameId = parameters["gameID"]?.toLongOrNull()
									?: throw BadRequestException("Invalid Game ID")
								val playerId = parameters["playerID"]?.toLongOrNull()
									?: throw BadRequestException("Invalid Player ID")
								return Player.find {
									PlayerTable.id eq playerId and (PlayerTable.gameCol eq gameId)
								}.limit(1).firstOrNull() ?: throw NotFoundException("Unable to find Player")
							}

							get {
								newSuspendedTransaction(db = Util.database) {
									call.respond(call.getPlayer().toJson(full = true))
								}
							}
							route(path = "/ticks") {
								get {
									newSuspendedTransaction(db = Util.database) {
										call.respond(call.getPlayer().ticks.map { it.toJson(full = true) })
									}
								}

								route(path = "/{tickID}") {
									fun ApplicationCall.getTick(): Tick {
										val gameId = parameters["gameID"]?.toLongOrNull()
											?: throw BadRequestException("Invalid Game ID")
										val playerId = parameters["playerID"]?.toLongOrNull()
											?: throw BadRequestException("Invalid Player ID")
										val tickId = parameters["tickID"]?.toLongOrNull()
											?: throw BadRequestException("Invalid Tick ID")
										return Tick.find {
											TickTable.id eq tickId and (TickTable.playerCol eq playerId) and (TickTable.gameCol eq gameId)
										}.limit(1).firstOrNull() ?: throw NotFoundException("Unable to find Tick")
									}

									get {
										newSuspendedTransaction(db = Util.database) {
											call.respond(call.getTick().toJson(full = true))
										}
									}
								}
							}
						}
					}
					route(path = "/teams") {
						get {
							newSuspendedTransaction(db = Util.database) {
								call.respond(call.getGame().teams.map { it.toJson(full = true) })
							}
						}
						route(path = "/{teamID}") {
							fun ApplicationCall.getTeam(): Team {
								val gameId = parameters["gameID"]?.toLongOrNull()
									?: throw BadRequestException("Invalid Game ID")
								val teamId = parameters["teamID"]?.toLongOrNull()
									?: throw BadRequestException("Invalid Team ID")
								return Team.find {
									TeamTable.id eq teamId and (TeamTable.gameCol eq gameId)
								}.limit(1).firstOrNull() ?: throw NotFoundException("Unable to find Team")
							}

							get {
								newSuspendedTransaction(db = Util.database) {
									call.respond(call.getTeam().toJson(full = true))
								}
							}
						}
					}
				}
			}
			route("/contributors") {
				get {
					val contributors = listOf(
						mapOf<String, Any?>(
							"Title" to "Macro303",
							"Image" to "macro303.png",
							"Role" to "Creator and Maintainer"
						).toSortedMap(),
						mapOf<String, Any?>(
							"Title" to "Miss. T",
							"Image" to "misst.jpg",
							"Role" to "Supporter and Loving Fiance"
						).toSortedMap(),
						mapOf<String, Any?>(
							"Title" to "Rocky",
							"Image" to "rocky.png",
							"Role" to "Quality Tester"
						).toSortedMap(),
						mapOf<String, Any?>(
							"Title" to "Img Bot App",
							"Image" to "imgbotapp.png",
							"Role" to "Image Processor"
						).toSortedMap()
					)
					call.respond(contributors)
				}
			}
		}
		static {
			defaultResource(resource = "/static/index.html")
			resources(resourcePackage = "static/images")
			resources(resourcePackage = "static/css")
			resources(resourcePackage = "static/js")
			resource(remotePath = "/navbar.html", resource = "static/navbar.html")
			resource(remotePath = "/players", resource = "static/players.html")
			resource(remotePath = "/players/{alias}", resource = "static/player.html")
			resource(remotePath = "/teams", resource = "static/teams.html")
			resource(remotePath = "/teams/{name}", resource = "static/team.html")
			resource(remotePath = "/about", resource = "static/about.html")
			resource(remotePath = "/Neptunes-Dashboard.yaml", resource = "static/Neptunes-Dashboard.yaml")
		}
		intercept(ApplicationCallPipeline.Fallback) {
			if (call.response.status() != null)
				application.log.info("${call.request.httpMethod.value.padEnd(4)}: ${call.response.status()} - ${call.request.uri}")
		}
	}
}