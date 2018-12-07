package macro.neptunes.core.game

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2018-Dec-04.
 */
object OfflineGame {
	private val LOGGER: Logger = LoggerFactory.getLogger(OfflineGame::class.java)
	val game: Game by lazy {
		LOGGER.info("Loading Offline Game")
		Game(name = "Offline Game", started = true, paused = true, totalStars = 12345)
	}
}