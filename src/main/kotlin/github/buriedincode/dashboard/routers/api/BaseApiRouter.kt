package github.buriedincode.dashboard.routers.api

import github.buriedincode.dashboard.Utils
import github.buriedincode.dashboard.models.IJson
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.NotFoundResponse
import org.apache.logging.log4j.kotlin.Logging
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass

abstract class BaseApiRouter<T : LongEntity>(protected val entity: LongEntityClass<T>) {
    protected val name: String = entity::class.java.declaringClass.simpleName.lowercase()
    protected val paramName: String = "$name-id"
    protected val title: String = name.replaceFirstChar(Char::uppercaseChar)

    companion object : Logging

    protected fun Context.getResource(): T {
        return this.pathParam(paramName).toLongOrNull()?.let {
            entity.findById(id = it) ?: throw NotFoundResponse(message = "$title not found")
        } ?: throw BadRequestResponse(message = "Invalid $title Id")
    }

    abstract fun listEndpoint(ctx: Context)

    abstract fun createEndpoint(ctx: Context)

    open fun getEndpoint(ctx: Context) {
        Utils.query {
            ctx.json((ctx.getResource() as IJson).toJson(showAll = true))
        }
    }

    abstract fun updateEndpoint(ctx: Context)

    open fun deleteEndpoint(ctx: Context) {
        Utils.query {
            ctx.getResource().delete()
        }
        ctx.status(status = HttpStatus.NO_CONTENT)
    }
}
