package macro303.neptunes.team

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import macro303.neptunes.Model
import macro303.neptunes.game.Game
import tornadofx.*
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by Macro303 on 2018-05-08.
 */
class TeamsModel : ViewModel(), Model {
	val teams = FXCollections.observableList(ArrayList<Team>())
	var game: Game? = null
		private set
	override val loading = SimpleBooleanProperty(false)
	private val exec: Executor

	init {
		exec = Executors.newCachedThreadPool { runnable ->
			val t = Thread(runnable)
			t.isDaemon = true
			t
		}
		updateModel()
	}

	@Suppress("UNCHECKED_CAST")
	override fun updateModel() {
		val refreshTask = TeamRefreshTask(this)
		refreshTask.setOnFailed {
			println("Unable to connect to Team API: ${refreshTask.exception.message}")
			loading.value = false
		}
		refreshTask.setOnSucceeded {
			this.teams.clear()
			val results = it.source.value as ArrayList<Team>?
			if (results != null) {
				this.teams.setAll(results)
				this.teams.sortDescending()
			}
			println("Team API loaded")
			loading.value = false
		}
		refreshTask.setOnCancelled {
			println("Unable to connect to Team API")
			loading.value = false
		}
		exec.execute(refreshTask)
	}

	override fun getAddress(): String {
		return "players"
	}

	companion object {
		private val dateFormat = DateTimeFormatter.ofPattern("MMM-dd HH:mm")
		private val timeFormat = DateTimeFormatter.ofPattern("MMM-dd HH:mm:ss")
	}
}