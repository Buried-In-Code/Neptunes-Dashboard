package macro303.neptunes_pride.tornadofx

import javafx.scene.text.FontWeight
import macro303.neptunes_pride.Connection
import tornadofx.*

internal class NeptunesStyles : Stylesheet() {
	private val regularButtonFont = loadFont(
		path = "/fonts/${Connection.config.regularButtonFontName}-Regular.ttf",
		size = 14
	)!!
	private val regularContentFont = loadFont(
		path = "/fonts/${Connection.config.regularContentFontName}-Regular.ttf",
		size = 14
	)!!
	private val boldContentFont = loadFont(
		path = "/fonts/${Connection.config.boldContentFontName}-Bold.ttf",
		size = 14
	)!!

	init {
		root {
			font = regularContentFont
			fontSize = Connection.config.contentSize
			fontWeight = FontWeight.NORMAL
		}
		button {
			font = regularButtonFont
			fontSize = Connection.config.buttonSize
			fontWeight = FontWeight.NORMAL
			prefWidth = 150.px
		}
		tab {
			font = regularButtonFont
			fontSize = Connection.config.buttonSize
			fontWeight = FontWeight.NORMAL
			prefWidth = 150.px
		}
		headerLabel {
			font = boldContentFont
			fontSize = Connection.config.headerSize
			fontWeight = FontWeight.BOLD
		}
		subHeaderLabel {
			font = boldContentFont
			fontSize = Connection.config.informationSize
			fontWeight = FontWeight.BOLD
		}
		informationLabel {
			font = regularContentFont
			fontSize = Connection.config.informationSize
			fontWeight = FontWeight.NORMAL
		}
		columnHeader {
			font = regularButtonFont
			fontSize = Connection.config.buttonSize
			fontWeight = FontWeight.NORMAL
		}
		tableCell {
			font = regularContentFont
			fontSize = Connection.config.contentSize
			fontWeight = FontWeight.NORMAL
		}
		strikethroughCell {
			font = regularContentFont
			fontSize = Connection.config.contentSize
			fontWeight = FontWeight.NORMAL
			text{
				strikethrough = true
			}
		}
	}

	companion object {
		val headerLabel by cssclass()
		val subHeaderLabel by cssclass()
		val informationLabel by cssclass()
		val strikethroughCell by cssclass()
	}
}