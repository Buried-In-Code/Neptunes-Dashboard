package macro.neptunes

import io.ktor.application.ApplicationCall

/**
 * Created by Macro303 on 2019-Feb-12.
 */
internal interface IRouter<T> {
	fun getAll(): List<T>
	suspend fun get(call: ApplicationCall): T
	suspend fun ApplicationCall.parseParam(): T
}