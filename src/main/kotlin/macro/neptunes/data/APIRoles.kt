package macro.neptunes.data

import io.javalin.security.Role

/**
 * Created by Macro303 on 2018-Nov-15.
 */
enum class APIRoles : Role {
	EVERYONE,
	DEVELOPER,
	ADMIN
}