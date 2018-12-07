package macro.neptunes.console

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.color.ANSIConstants.*
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase

/**
 * Created by Macro303 on 2018-Nov-27.
 */
class HighlightLogs : ForegroundCompositeConverterBase<ILoggingEvent>() {
	override fun getForegroundColorCode(event: ILoggingEvent): String {
		return when (event.level) {
			Level.ERROR -> RED_FG
			Level.WARN -> YELLOW_FG
			Level.INFO -> DEFAULT_FG
			Level.DEBUG -> CYAN_FG
			else -> BLUE_FG
		}
	}
}