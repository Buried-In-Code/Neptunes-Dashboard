package macro.neptunes.console

/**
 * Created by Macro303 on 2018-Nov-09.
 */
internal enum class Colour(val ansiCode: String) {
	RESET(ansiCode = "\u001B[0m"),
	BLACK(ansiCode = "\u001B[30m"),
	RED(ansiCode = "\u001B[31m"),
	GREEN(ansiCode = "\u001B[32m"),
	YELLOW(ansiCode = "\u001B[33m"),
	BLUE(ansiCode = "\u001B[34m"),
	MAGENTA(ansiCode = "\u001B[35m"),
	CYAN(ansiCode = "\u001B[36m"),
	WHITE(ansiCode = "\u001B[37m");

	override fun toString(): String {
		return ansiCode
	}
}