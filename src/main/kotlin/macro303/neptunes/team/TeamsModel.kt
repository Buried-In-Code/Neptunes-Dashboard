package macro303.neptunes.team

import javafx.collections.FXCollections
import javafx.util.Duration
import macro303.neptunes.Model
import macro303.neptunes.game.Game
import tornadofx.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by Macro303 on 2018-05-08.
 */
class TeamsModel : ViewModel(), Model {
	val teams = FXCollections.observableList(ArrayList<Team>())
	var game: Game? = null
		private set

	init {
		val refreshService = TeamRefreshService(this)
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
			println("Team Update At: ${LocalDateTime.now().format(timeFormat)}")
		}
		refreshService.start()
	}

	@Suppress("UNCHECKED_CAST")
	override fun updateModel(any: Any) {
		val teams = any as ArrayList<Team>
		this.teams.clear()
		this.teams.setAll(teams)
		this.teams.sortDescending()
	}

	override fun getAddress(): String {
		return "players"
	}

	companion object {
		private val dateFormat = DateTimeFormatter.ofPattern("MMM-dd HH:mm")
		private val timeFormat = DateTimeFormatter.ofPattern("MMM-dd HH:mm:ss")
	}
}