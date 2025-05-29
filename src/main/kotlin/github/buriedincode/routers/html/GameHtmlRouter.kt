package github.buriedincode.routers.html

import github.buriedincode.Utils.transaction
import github.buriedincode.models.Game
import io.github.oshai.kotlinlogging.KotlinLogging
import io.javalin.http.Context

object GameHtmlRouter : BaseHtmlRouter<Game>(entity = Game) {
  @JvmStatic private val LOGGER = KotlinLogging.logger {}

  override fun viewEndpoint(ctx: Context): Unit = transaction {
    ctx.render(
      filePath = "templates/$name/view.kte",
      model = mapOf("games" to Game.all().toList(), "resource" to ctx.getResource()),
    )
  }
}
