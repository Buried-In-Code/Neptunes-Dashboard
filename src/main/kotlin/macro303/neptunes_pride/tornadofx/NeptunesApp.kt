package macro303.neptunes_pride.tornadofx

import javafx.stage.Stage
import macro303.neptunes_pride.Config
import tornadofx.*
import java.util.*

internal class NeptunesApp : App(primaryView = NeptunesView::class, stylesheet = NeptunesStyles::class) {
	init {
		FX.locale = Locale.ENGLISH
		reloadStylesheetsOnFocus()
	}

	override fun start(stage: Stage) {
		super.start(stage)
		stage.isMaximized = true
	}
}