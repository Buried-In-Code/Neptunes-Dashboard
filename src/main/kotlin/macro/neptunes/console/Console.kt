package macro.neptunes.console

import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-09.
 */
internal object Console {
	private val LOGGER = LogManager.getLogger(Console::class.java)
	internal val HEADING: Colour = Colour.CYAN
	internal val HIGHLIGHT: Colour = Colour.MAGENTA
	internal val STANDARD: Colour = Colour.WHITE

	internal fun displayHeading(text: String) {
		colourText(text = "=".repeat(text.length + 4), colour = HEADING)
		displaySubHeading(text = text)
		colourText(text = "=".repeat(text.length + 4), colour = HEADING)
	}

	internal fun displaySubHeading(text: String) {
		colourText(text = "  $text  ", colour = HEADING)
	}

	internal fun display(text: String, colour: Colour = STANDARD) {
		colourText(text = text, colour = colour)
	}

	internal fun displayLeaderboard(headers: List<String>, data: List<List<Any>>) {
		val sizes = ArrayList<Int>()
		headers.forEachIndexed { index, value ->
			var maxSize = value.length
			data.forEach {
				if (it[index].toString().length > maxSize)
					maxSize = it[index].toString().length
			}
			sizes.add(maxSize)
		}
		var boarder = "+"
		sizes.forEach {
			boarder += ("-".repeat(it + 2)) + "+"
		}
		var header = "$HIGHLIGHT|"
		headers.forEachIndexed { index, value ->
			header += colourEntry(text = value, colour = HEADING, size = sizes[index])
		}
		colourText(text = boarder, colour = HIGHLIGHT)
		colourText(text = header, colour = HEADING)
		colourText(text = boarder, colour = HIGHLIGHT)
		data.forEach {
			var colour = STANDARD
			it.forEach { value ->
				if (value.toString() == false.toString())
					colour = Colour.RED
			}
			var dataLine = "$HIGHLIGHT|"
			it.forEachIndexed { index, value ->
				dataLine += colourEntry(text = value, colour = colour, padEnd = index == 0, size = sizes[index])
			}
			colourText(text = dataLine, colour = STANDARD)
		}
		colourText(text = boarder, colour = HIGHLIGHT)
	}

	private fun colourText(text: String?, colour: Colour) {
		println("$colour$text${Colour.RESET}")
		var temp = text
		Colour.values().forEach {
			temp = temp?.replace(it.ansiCode, "")
		}
		LOGGER.trace(temp)
	}

	private fun colourEntry(text: Any?, colour: Colour, padEnd: Boolean = true, size: Int): String {
		if (padEnd)
			return "$colour ${text.toString().padEnd(size)} $HIGHLIGHT|${Colour.RESET}"
		return "$colour ${text.toString().padStart(size)} $HIGHLIGHT|${Colour.RESET}"
	}
}