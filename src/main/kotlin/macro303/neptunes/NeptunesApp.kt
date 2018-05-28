package macro303.neptunes

import javafx.stage.Stage
import tornadofx.*

/**
 * Created by Macro303 on 2018-05-08.
 */
class NeptunesApp : App(primaryView = NeptunesView::class) {
	init {
//		importStylesheet(stylesheet = "/css/modena_dark.css")
//		importStylesheet(stylesheet = "/css/bootstrap3.css")
		importStylesheet(stylesheet = "/css/MaterialFX-v0.3.css")
//		importStylesheet(stylesheet = "/css/sdkfx.css")
	}

	override fun start(stage: Stage) {
		super.start(stage)
		stage.isMaximized = true
	}
}