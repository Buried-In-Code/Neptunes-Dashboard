package macro303.neptunes_pride.tornadofx

import javafx.scene.text.FontWeight
import macro303.neptunes_pride.Connection
import tornadofx.*

internal class NeptunesStyles : Stylesheet() {
	private val regularButtonFont = loadFont(
		path = "/fonts/${Connection.configProperty?.value?.regularButtonFontName ?: "OverlockSC"}-Regular.ttf",
		size = 14
	)!!
	private val regularContentFont = loadFont(
		path = "/fonts/${Connection.configProperty?.value?.regularContentFontName ?: "LifeSavers"}-Regular.ttf",
		size = 14
	)!!
	private val boldContentFont = loadFont(
		path = "/fonts/${Connection.configProperty?.value?.boldContentFontName ?: "LifeSavers"}-Bold.ttf",
		size = 14
	)!!

	init {
		root {
			font = regularContentFont
			fontSize = Connection.configProperty.value.contentSize
			fontWeight = FontWeight.NORMAL
		}
		button {
			font = regularButtonFont
			fontSize = Connection.configProperty.value.buttonSize
			fontWeight = FontWeight.NORMAL
			prefWidth = 150.px
		}
		tab {
			font = regularButtonFont
			fontSize = Connection.configProperty.value.buttonSize
			fontWeight = FontWeight.NORMAL
			prefWidth = 150.px
		}
		headerLabel {
			font = boldContentFont
			fontSize = Connection.configProperty.value.headerSize
			fontWeight = FontWeight.BOLD
		}
		subHeaderLabel {
			font = boldContentFont
			fontSize = Connection.configProperty.value.informationSize
			fontWeight = FontWeight.BOLD
		}
		informationLabel {
			font = regularContentFont
			fontSize = Connection.configProperty.value.informationSize
			fontWeight = FontWeight.NORMAL
		}
		columnHeader {
			font = regularButtonFont
			fontSize = Connection.configProperty.value.buttonSize
			fontWeight = FontWeight.NORMAL
		}
		tableCell {
			font = regularContentFont
			fontSize = Connection.configProperty.value.contentSize
			fontWeight = FontWeight.NORMAL
		}
	}

	companion object {
		val headerLabel by cssclass()
		val subHeaderLabel by cssclass()
		val informationLabel by cssclass()
	}
}