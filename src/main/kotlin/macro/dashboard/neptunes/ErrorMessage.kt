package macro.dashboard.neptunes

import io.ktor.http.HttpStatusCode

/**
 * Created by Macro303 on 2018-Nov-22.
 */
data class ErrorMessage(
	val request: String,
	val message: String,
	val code: HttpStatusCode,
	val cause: Throwable? = null
)