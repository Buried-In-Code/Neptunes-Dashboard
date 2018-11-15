package macro.neptunes.data

/**
 * Created by Macro303 on 2018-Nov-14.
 */
enum class ContentType(val value: String) {
	TEXT(value = "text/plain"),
	JSON(value = "application/json"),
	HTML(value = "text/HTML");

	override fun toString(): String {
		return value
	}
}