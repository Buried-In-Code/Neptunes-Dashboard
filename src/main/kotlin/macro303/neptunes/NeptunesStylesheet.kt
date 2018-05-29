package macro303.neptunes

import javafx.scene.text.FontWeight
import tornadofx.*

/**
 * Created by Macro303 on 2018-05-29.
 */
class NeptunesStylesheet : Stylesheet() {
	private val headerFont = loadFont(path = "/fonts/LifeSavers-Bold.ttf", size = 14)!!
	private val buttonFont = loadFont(path = "/fonts/OverlockSC-Regular.ttf", size = 14)!!
	private val myFont = loadFont(path = "/fonts/Overlock-Regular.ttf", size = 14)!!
	private val headerFontSize = 18.px
	private val buttonFontSize = 16.px
	private val myFontSize = 14.px

	init {
		root {
			font = myFont
			fontSize = myFontSize
		}
		button {
			font = buttonFont
			fontSize = buttonFontSize
			prefWidth = 150.px
		}
		headerLabel {
			font = headerFont
			fontWeight = FontWeight.BOLD
			fontSize = headerFontSize
		}
		subHeaderLabel {
			font = headerFont
			fontWeight = FontWeight.BOLD
			fontSize = myFontSize
		}
		columnHeader{
			font = buttonFont
			fontSize = buttonFontSize
		}
	}

	companion object {
		val headerLabel by cssclass()
		val subHeaderLabel by cssclass()
	}
}