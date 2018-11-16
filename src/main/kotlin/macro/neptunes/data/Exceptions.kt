package macro.neptunes.data

import io.javalin.Context
import macro.neptunes.Application

/**
 * Created by Macro303 on 2018-Nov-14.
 */
internal object Exceptions {
	internal fun contentType(context: Context) {
		val message = "'${context.path()}' doesn't support '${context.header("Content-Type")}' Content-Type"
		if (context.path().startsWith("/api")) {
			val details = mapOf(Pair("Error", message))
			context.json(details)
		} else {
			val details = "<html><h1>$message</h1></html>"
			context.html(details)
		}
		context.status(400)
	}

	internal fun missingParams(vararg param: String, context: Context, isOr: Boolean = true) {
		val missing = if (isOr) param.joinToString(" or ") { "'$it'" } else param.joinToString(" and ") { "'$it'" }
		val message = "$missing is required in your request"
		if (context.header("Content-Type") == Application.JSON || context.path().startsWith("/api")) {
			val details = mapOf(Pair("Error", message))
			context.json(details)
		} else {
			val details = "<html><h1>$message</h1></html>"
			context.html(details)
		}
		context.status(400)
	}

	internal fun notYetAvailable(context: Context) {
		val message = "'${context.path()}' is not yet available, watch this space"
		if (context.header("Content-Type") == Application.JSON || context.path().startsWith("/api")) {
			val details = mapOf(Pair("Error", message))
			context.json(details)
		} else {
			val details = "<html><h1>$message</h1></html>"
			context.html(details)
		}
		context.status(418)
	}

	internal fun invalidParam(context: Context, param: String) {
		val message = "'$param' is not a valid parameter for '${context.path()}'"
		if (context.header("Content-Type") == Application.JSON || context.path().startsWith("/api")) {
			val details = mapOf(Pair("Error", message))
			context.json(details)
		} else {
			val details = "<html><h1>$message</h1></html>"
			context.html(details)
		}
		context.status(400)
	}

	internal fun illegalAccess(context: Context) {
		val message = "'${context.path()}' isn't for you"
		if (context.header("Content-Type") == Application.JSON) {
			val details = mapOf(Pair("Error", message))
			context.json(details)
		} else {
			val details = "<html><h1>$message</h1></html>"
			context.html(details)
		}
		context.status(401)
	}
}