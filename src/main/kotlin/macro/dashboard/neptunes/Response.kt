package macro.dashboard.neptunes

import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.Accepted
import io.ktor.http.HttpStatusCode.Companion.BadGateway
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.Forbidden
import io.ktor.http.HttpStatusCode.Companion.Found
import io.ktor.http.HttpStatusCode.Companion.GatewayTimeout
import io.ktor.http.HttpStatusCode.Companion.Gone
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.MethodNotAllowed
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.ServiceUnavailable
import io.ktor.http.HttpStatusCode.Companion.Unauthorized

/**
 * Last Updated by Macro303 on 2019-May-14
 */
open class HttpResponseException(
	val status: HttpStatusCode,
	@Deprecated("Use 'message' instead") val msg: String
) : RuntimeException(msg)

class BadRequestResponse(message: String = BadRequest.description) :
	HttpResponseException(BadRequest, message)

class NotFoundResponse(message: String = NotFound.description) :
	HttpResponseException(NotFound, message)

class MethodNotAllowedResponse(message: String = MethodNotAllowed.description) :
	HttpResponseException(MethodNotAllowed, message)

class ConflictResponse(message: String = Conflict.description) :
	HttpResponseException(Conflict, message)

class GoneResponse(message: String = Gone.description) :
	HttpResponseException(Gone, message)

class InternalServerErrorResponse(message: String = InternalServerError.description) :
	HttpResponseException(InternalServerError, message)

class NotYetImplementedResponse(message: String = "Not Yet Implemented") :
	HttpResponseException(Accepted, message)