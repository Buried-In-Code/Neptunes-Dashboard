package macro.neptunes.data

import io.javalin.Context

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object WelcomeController {

	private fun getMessage(): String {
		return "Welcome to BIT 269's Neptune's Pride API"
	}

	fun webGet(context: Context) {
		context.html("<html><h1>${getMessage()}</h1></html>")
	}

	fun apiGet(context: Context) {
		if (context.status() >= 400)
			return
		context.json(mapOf(Pair("Message", getMessage())))
	}
}