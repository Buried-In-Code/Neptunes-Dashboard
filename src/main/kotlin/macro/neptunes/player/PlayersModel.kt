package macro.neptunes.player

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import tornadofx.*
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by Macro303 on 2018-05-08.
 */
class PlayersModel : ViewModel(), macro.neptunes.Model {
	val players = FXCollections.observableList(ArrayList<Player>())
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
		val refreshTask = PlayerRefreshTask(this)
		refreshTask.setOnFailed {
			println("Unable to connect to Player API: ${refreshTask.exception.message}")
			loading.value = false
		}
		refreshTask.setOnSucceeded {
			this.players.clear()
			val results = it.source.value as ArrayList<Player>?
			if (results != null) {
				this.players.setAll(results)
				this.players.sortDescending()
			}
			println("Player API loaded")
			loading.value = false
		}
		refreshTask.setOnCancelled {
			println("Unable to connect to Player API")
			loading.value = false
		}
		loading.value = true
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