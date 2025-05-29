package github.buriedincode

import gg.jte.ContentType as JteType
import gg.jte.TemplateEngine
import github.buriedincode.Utils.log
import github.buriedincode.Utils.toHumanReadable
import github.buriedincode.routers.api.GameApiRouter
import github.buriedincode.routers.html.GameHtmlRouter
import github.buriedincode.routers.html.PlayerHtmlRouter
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.ApiBuilder.put
import io.javalin.http.ContentType
import io.javalin.rendering.FileRenderer
import io.javalin.rendering.template.JavalinJte
import java.nio.file.Path

object App {
  @JvmStatic private val LOGGER = KotlinLogging.logger {}

  private fun createJavalinApp(fileRenderer: FileRenderer): Javalin {
    return Javalin.create {
      it.fileRenderer(fileRenderer = fileRenderer)
      it.http.prefer405over404 = true
      it.http.defaultContentType = ContentType.JSON
      it.requestLogger.http { ctx, ms ->
        val level =
          when {
            ctx.statusCode() in (100..<200) -> Level.WARN
            ctx.statusCode() in (200..<300) -> Level.INFO
            ctx.statusCode() in (300..<400) -> Level.INFO
            ctx.statusCode() in (400..<500) -> Level.WARN
            else -> Level.ERROR
          }
        LOGGER.log(level) { "${ctx.statusCode()}: ${ctx.method()} - ${ctx.path()} => ${toHumanReadable(ms)}" }
      }
      it.router.ignoreTrailingSlashes = true
      it.router.treatMultipleSlashesAsSingleSlash = true
      it.router.caseInsensitiveRoutes = true
      it.router.apiBuilder {
        path("/") {
          get(GameHtmlRouter::listEndpoint)
          path("games") {
            path("{game-id}") {
              get(GameHtmlRouter::viewEndpoint)
              path("players") {
                get(PlayerHtmlRouter::listEndpoint)
                path("{player-id}") { get(PlayerHtmlRouter::viewEndpoint) }
              }
            }
          }
          path("api") {
            path("games") {
              get(GameApiRouter::listEndpoint)
              post(GameApiRouter::createEndpoint)
              path("{game-id}") {
                get(GameApiRouter::getEndpoint)
                put(GameApiRouter::updateEndpoint)
              }
            }
          }
        }
      }
      it.staticFiles.add {
        it.hostedPath = "/static"
        it.directory = "/static"
      }
    }
  }

  fun start(settings: Settings) {
    val engine = TemplateEngine.createPrecompiled(Path.of("jte-classes"), JteType.Html)
    engine.setTrimControlStructures(true)
    val renderer = JavalinJte(engine)

    val app = createJavalinApp(fileRenderer = renderer)
    app.start(settings.website.host, settings.website.port)
  }
}

fun main(@Suppress("UNUSED_PARAMETER") vararg args: String) {
  println("Neptune's Dashboard v${VERSION}")
  println("Kotlin v${KotlinVersion.CURRENT}")
  println("Java v${System.getProperty("java.version")}")
  println("Arch: ${System.getProperty("os.arch")}")

  val settings = Settings.load()
  println(settings)

  App.start(settings = settings)
}
