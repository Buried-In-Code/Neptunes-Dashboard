package macro303.neptunes_pride.tornadofx

import javafx.scene.text.FontWeight
import tornadofx.*

internal class NeptunesStyles : Stylesheet() {
	private val regularButtonFont = loadFont(path = "/fonts/OverlockSC-Regular.ttf", size = 14)!!
	private val regularCustomFont = loadFont(path = "/fonts/LifeSavers-Regular.ttf", size = 14)!!
	private val boldCustomFont = loadFont(path = "/fonts/LifeSavers-Bold.ttf", size = 14)!!

	private val headerFontSize = 40.px
	private val buttonFontSize = 22.px
	private val customFontSize = 22.px
	private val contentFontSize = 16.px

	init {
		root {
			font = regularCustomFont
			fontSize = contentFontSize
			fontWeight = FontWeight.NORMAL
		}
		button {
			font = regularButtonFont
			fontSize = buttonFontSize
			fontWeight = FontWeight.NORMAL
			prefWidth = 150.px
		}
		tab {
			font = regularButtonFont
			fontSize = buttonFontSize
			fontWeight = FontWeight.NORMAL
			prefWidth = 150.px
		}
		headerLabel {
			font = boldCustomFont
			fontSize = headerFontSize
			fontWeight = FontWeight.BOLD
		}
		subHeaderLabel {
			font = boldCustomFont
			fontSize = customFontSize
			fontWeight = FontWeight.BOLD
		}
		informationLabel {
			font = regularCustomFont
			fontSize = customFontSize
			fontWeight = FontWeight.NORMAL
		}
		columnHeader{
			font = regularButtonFont
			fontSize = buttonFontSize
			fontWeight = FontWeight.NORMAL
		}
		tableCell{
			font = regularCustomFont
			fontSize = contentFontSize
			fontWeight = FontWeight.NORMAL
		}
	}

	companion object {
		val headerLabel by cssclass()
		val subHeaderLabel by cssclass()
		val informationLabel by cssclass()
	}
}