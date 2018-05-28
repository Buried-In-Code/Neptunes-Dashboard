package macro303.neptunes.player

import javafx.collections.FXCollections
import javafx.util.Duration
import macro303.neptunes.Model
import tornadofx.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by Macro303 on 2018-05-08.
 */
class PlayersModel : ViewModel(), Model {
	val players = FXCollections.observableList(ArrayList<Player>())

	init {
		val refreshService = PlayerRefreshService(this)
		refreshService.period = Duration.hours(1.0)
		refreshService.setOnFailed {
			println("Exception Loading API: ${it.source.exception.localizedMessage}")
		}
		refreshService.setOnCancelled {
			println("Unable to connect to API")
		}
		refreshService.setOnSucceeded {
			val players = it.source.value
			if (players != null)
				updateModel(any = players)
			println("Player Update At: ${LocalDateTime.now().format(timeFormat)}")
		}
		refreshService.start()
	}

	@Suppress("UNCHECKED_CAST")
	override fun updateModel(any: Any) {
		val players = any as ArrayList<Player>
		this.players.clear()
		this.players.setAll(players)
		this.players.sortDescending()
	}

	override fun getAddress(): String {
		return "players"
	}

	companion object {
		private val dateFormat = DateTimeFormatter.ofPattern("MMM-dd HH:mm")
		private val timeFormat = DateTimeFormatter.ofPattern("MMM-dd HH:mm:ss")
	}
}