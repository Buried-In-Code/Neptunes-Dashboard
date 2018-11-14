package macro.neptunes.data

import macro.neptunes.core.Util.toJSON
import macro.neptunes.data.ContentType.JSON
import macro.neptunes.data.ContentType.TEXT
import spark.Request
import spark.Response

/**
 * Created by Macro303 on 2018-Nov-14.
 */
internal object Exceptions {
	internal fun contentType(request: Request, response: Response) {
		response.type(JSON.value)
		val details = mapOf(Pair("Error", "'${request.pathInfo()}' doesn't support '${request.contentType()}' Content-Type")).toJSON()
		response.body(details)
		response.status(400)
	}

	internal fun forbidden(request: Request, response: Response) {
		response.type(JSON.value)
		val details = mapOf(Pair("Error", "'${request.pathInfo()}' isn't for you")).toJSON()
		response.body(details)
		response.status(403)
	}

	internal fun missingParam(request: Request, response: Response, param: String) {
		var message = "You are missing '$param' from your request"
		when {
			request.contentType() == TEXT.value || request.contentType() == null -> response.type(request.contentType())
			else -> {
				response.type(JSON.value)
				message = mapOf(Pair("Error", message)).toJSON()
			}
		}
		response.body(message)
		response.status(400)
	}

	internal fun missingParams(vararg param: String, request: Request, response: Response, isOr: Boolean = true) {
		val missing = if (isOr) param.joinToString(" or ") { "'$it'" } else param.joinToString(" and ") { "'$it'" }
		var message = "$missing is required in your request"
		when {
			request.contentType() == TEXT.value || request.contentType() == null -> response.type(request.contentType())
			else -> {
				response.type(JSON.value)
				message = mapOf(Pair("Error", message)).toJSON()
			}
		}
		response.body(message)
		response.status(400)
	}
}