package macro.neptunes.data

/**
 * Created by Macro303 on 2018-Nov-14.
 */
internal object Exceptions {

	/*internal fun notYetAvailable(context: Context) {
		val message = "'${context.path()}' is not yet available, watch this space"
		if (context.header("Content-Type") == Application.JSON) {
			val details = mapOf(Pair("Error", message))
			context.json(details)
		} else {
			val details = "<html><h3>$message</h3></html>"
			context.html(details)
		}
		context.status(418)
	}

	internal fun invalidParam(context: Context, param: String) {
		val message = "'$param' is not a valid parameter for '${context.path()}'"
		if (context.header("Content-Type") == Application.JSON) {
			val details = mapOf(Pair("Error", message))
			context.json(details)
		} else {
			val details = "<html><h3>$message</h3></html>"
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
			val details = "<html><h3>$message</h3></html>"
			context.html(details)
		}
		context.status(401)
	}*/
}