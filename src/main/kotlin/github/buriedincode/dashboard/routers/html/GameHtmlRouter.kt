package github.buriedincode.dashboard.routers.html

import github.buriedincode.dashboard.Utils
import github.buriedincode.dashboard.models.Game
import io.javalin.http.Context
import org.apache.logging.log4j.kotlin.Logging

object GameHtmlRouter : BaseHtmlRouter<Game>(entity = Game), Logging {
    override fun viewEndpoint(ctx: Context) {
        Utils.query {
            ctx.render(
                filePath = "templates/$name/view.kte",
                model = mapOf(
                    "games" to Game.all().toList(),
                    "resource" to ctx.getResource(),
                ),
            )
        }
    }
}
