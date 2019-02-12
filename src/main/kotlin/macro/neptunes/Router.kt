package macro.neptunes

import io.ktor.application.ApplicationCall
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.HttpStatusCode
import io.ktor.request.httpMethod
import io.ktor.response.respond
import java.util.*

/**
 * Created by Macro303 on 2019-Feb-12.
 */
abstract class Router<T> {

	abstract fun getAll(): List<T>
	abstract suspend fun get(call: ApplicationCall, useJson: Boolean = true): T?
	abstract suspend fun ApplicationCall.parseParam(useJson: Boolean = true): T?
	abstract suspend fun ApplicationCall.parseBody(useJson: Boolean = true): Map<String, Any?>?

	protected suspend fun ApplicationCall.notFound(useJson: Boolean = true, type: String, field: String, value: Any?) {
		val message = ErrorMessage(
			code = HttpStatusCode.NotFound,
			request = "${request.httpMethod.value} ${request.local.uri}",
			message = "No $type was found with the $field: $value"
		)
		generateResponse(call = this, useJson = useJson, message = message)
	}

	protected suspend fun ApplicationCall.badRequest(useJson: Boolean = true, fields: Array<String>, values: Array<Any?>) {
		val message = ErrorMessage(
			code = HttpStatusCode.BadRequest,
			request = "${request.httpMethod.value} ${request.local.uri}",
			message = "[${Arrays.toString(fields)}] all are required to be valid, however [${Arrays.toString(values)}] were found"
		)
		generateResponse(call = this, useJson = useJson, message = message)
	}

	protected suspend fun ApplicationCall.conflict(useJson: Boolean = true, type: String, field: String, value: Any?) {
		val message = ErrorMessage(
			code = HttpStatusCode.Conflict,
			request = "${request.httpMethod.value} ${request.local.uri}",
			message = "A $type was already found with the $field: $value"
		)
		generateResponse(call = this, useJson = useJson, message = message)
	}

	private suspend fun generateResponse(call: ApplicationCall, useJson: Boolean = true, message: ErrorMessage) {
		when (useJson) {
			true -> call.respond(
				message = message,
				status = message.code
			)
			false -> call.respond(
				message = FreeMarkerContent(template = "Exception.ftl", model = message),
				status = message.code
			)
		}
	}
}