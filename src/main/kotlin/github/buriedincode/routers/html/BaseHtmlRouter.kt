package github.buriedincode.routers.html

import github.buriedincode.Utils.transaction
import io.github.oshai.kotlinlogging.KotlinLogging
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass

abstract class BaseHtmlRouter<T : LongEntity>(protected val entity: LongEntityClass<T>) {
  protected val name: String = entity::class.java.declaringClass.simpleName.lowercase()
  protected val paramName: String = "$name-id"
  protected val title: String = name.replaceFirstChar(Char::uppercaseChar)

  companion object {
    @JvmStatic private val LOGGER = KotlinLogging.logger {}
  }

  protected fun Context.getResource(): T =
    this.pathParam(paramName).toLongOrNull()?.let {
      entity.findById(id = it) ?: throw NotFoundResponse(message = "$title not found")
    } ?: throw BadRequestResponse(message = "Invalid $title Id")

  open fun listEndpoint(ctx: Context): Unit = transaction {
    ctx.render(filePath = "templates/$name/list.kte", model = mapOf("resources" to entity.all().toList()))
  }

  open fun createEndpoint(ctx: Context): Unit = transaction { ctx.render(filePath = "templates/$name/create.kte") }

  open fun viewEndpoint(ctx: Context): Unit = transaction {
    ctx.render(filePath = "templates/$name/view.kte", model = mapOf("resource" to ctx.getResource()))
  }

  open fun updateEndpoint(ctx: Context): Unit = transaction {
    ctx.render(filePath = "templates/$name/update.kte", model = mapOf("resource" to ctx.getResource()))
  }
}
