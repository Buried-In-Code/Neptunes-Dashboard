package macro.neptunes.data

import io.javalin.Context
import macro.neptunes.core.Util

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object HelpController {

	fun get(context: Context){
		context.html(Util.htmlToString(location = "help"))
	}
}