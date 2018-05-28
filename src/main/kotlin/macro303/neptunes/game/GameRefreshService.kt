package macro303.neptunes.game

import com.google.gson.GsonBuilder
import javafx.concurrent.ScheduledService
import javafx.concurrent.Task
import macro303.neptunes.Model
import macro303.neptunes.Util
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL

class GameRefreshService(private val model: Model) : ScheduledService<Game?>() {

	override fun createTask(): Task<Game?> {
		return object : Task<Game?>() {
			override fun call(): Game? {
				var connection: HttpURLConnection? = null
				try {
					connection = getConnection()
					if (connection != null) {
						println("Response Code: ${connection.responseCode}")
						if (connection.responseCode == 200)
							return gson.fromJson(InputStreamReader(connection.inputStream), Game::class.java)
					}
				} finally {
					connection?.disconnect()
				}
				cancel()
				return null
			}
		}
	}

	private fun getConnection(): HttpURLConnection? {
		println("Address: ${Util.API}${Util.config.gameID}/${model.getAddress()}")
		val url = URL("${Util.API}${Util.config.gameID}/${model.getAddress()}")
		var connection: HttpURLConnection?
		connection = if (Util.config.proxy == null)
			url.openConnection() as HttpURLConnection
		else
			url.openConnection(Util.config.proxy!!) as HttpURLConnection
		connection.requestMethod = "GET"
		try {
			connection.connect()
			println("Connected Successfully")
		} catch (ce: ConnectException) {
			connection = null
			println("Connected Unsuccessfully: ${ce.message}")
		}
		return connection
	}

	companion object {
		private val gson = GsonBuilder()
			.registerTypeAdapter(Game::class.java, GameAdapter())
			.serializeNulls()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create()
	}
}