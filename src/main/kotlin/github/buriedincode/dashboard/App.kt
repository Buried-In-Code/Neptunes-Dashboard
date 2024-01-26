package github.buriedincode.dashboard

import gg.jte.TemplateEngine
import gg.jte.resolve.DirectoryCodeResolver
import github.buriedincode.dashboard.routers.api.GameApiRouter
import github.buriedincode.dashboard.routers.html.GameHtmlRouter
import github.buriedincode.dashboard.routers.html.PlayerHtmlRouter
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.ApiBuilder.put
import io.javalin.http.ContentType
import io.javalin.rendering.template.JavalinJte
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.kotlin.Logging
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.div
import gg.jte.ContentType as JteType

object App : Logging {
    private fun createTemplateEngine(environment: Settings.Environment): TemplateEngine {
        return if (environment == Settings.Environment.DEV) {
            val codeResolver = DirectoryCodeResolver(Path.of("src", "main", "jte"))
            TemplateEngine.create(codeResolver, JteType.Html)
        } else {
            TemplateEngine.createPrecompiled(Path.of("jte-classes"), JteType.Html)
        }
    }

    private fun createJavalinApp(): Javalin {
        return Javalin.create {
            it.http.prefer405over404 = true
            it.http.defaultContentType = ContentType.JSON
            it.requestLogger.http { ctx, ms ->
                val level = when {
                    ctx.statusCode() in (100..<200) -> Level.WARN
                    ctx.statusCode() in (200..<300) -> Level.INFO
                    ctx.statusCode() in (300..<400) -> Level.INFO
                    ctx.statusCode() in (400..<500) -> Level.WARN
                    else -> Level.ERROR
                }
                logger.log(level, "${ctx.statusCode()}: ${ctx.method()} - ${ctx.path()} => ${Utils.toHumanReadable(ms)}")
            }
            it.routing.ignoreTrailingSlashes = true
            it.routing.treatMultipleSlashesAsSingleSlash = true
            it.routing.caseInsensitiveRoutes = true
            it.staticFiles.add {
                it.hostedPath = "/static"
                it.directory = "/static"
            }
        }
    }

    fun start(settings: Settings) {
        val engine = createTemplateEngine(environment = settings.environment)
        // engine.setTrimControlStructures = true
        JavalinJte.init(templateEngine = engine)

        val app = createJavalinApp()
        app.routes {
            path("/") {
                get(GameHtmlRouter::listEndpoint)
            }
            path("games") {
                path("{game-id}") {
                    get(GameHtmlRouter::viewEndpoint)
                    path("players") {
                        get(PlayerHtmlRouter::listEndpoint)
                        path("{player-id}") {
                            get(PlayerHtmlRouter::viewEndpoint)
                        }
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
        app.start(settings.website.host, settings.website.port)
    }
}

fun main(
    @Suppress("UNUSED_PARAMETER") vararg args: String,
) {
    println("Neptunes Dashboard v${Utils.VERSION}")
    println("Kotlin v${KotlinVersion.CURRENT}")
    println("Java v${System.getProperty("java.version")}")
    println("Arch: ${System.getProperty("os.arch")}")

    val settings = Settings.load()
    println(settings)

    App.start(settings = settings)
}
