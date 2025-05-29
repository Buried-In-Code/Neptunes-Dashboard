package github.buriedincode.routers.api

import github.buriedincode.Utils.transaction
import github.buriedincode.models.IJson
import io.github.oshai.kotlinlogging.KotlinLogging
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.NotFoundResponse
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass

abstract class BaseApiRouter<T : LongEntity>(protected val entity: LongEntityClass<T>) {
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

  abstract fun listEndpoint(ctx: Context): Unit

  abstract fun createEndpoint(ctx: Context): Unit

  open fun getEndpoint(ctx: Context): Unit = transaction {
    ctx.json((ctx.getResource() as IJson).toJson(showAll = true))
  }

  abstract fun updateEndpoint(ctx: Context): Unit

  open fun deleteEndpoint(ctx: Context): Unit {
    transaction { ctx.getResource().delete() }
    ctx.status(status = HttpStatus.NO_CONTENT)
  }
}
