package macro303.neptunes.player

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import javafx.concurrent.ScheduledService
import javafx.concurrent.Task
import macro303.neptunes.Model
import macro303.neptunes.Util
import macro303.neptunes.technology.Technology
import macro303.neptunes.technology.TechnologyAdapter
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class PlayerRefreshService(private val model: Model) : ScheduledService<ArrayList<Player>?>() {

	override fun createTask(): Task<ArrayList<Player>?> {
		return object : Task<ArrayList<Player>?>() {
			override fun call(): ArrayList<Player>? {
				var connection: HttpURLConnection? = null
				try {
					connection = getConnection()
					if (connection != null) {
						println("Response Code: ${connection.responseCode}")
						if (connection.responseCode == 200) {
							val playerWrapper =
								gson.fromJson<HashMap<String, Player>>(InputStreamReader(connection.inputStream))
							return playerWrapper.values.toCollection(ArrayList())
						}
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

	inline fun <reified T> Gson.fromJson(stream: InputStreamReader) =
		this.fromJson<T>(stream, object : TypeToken<T>() {}.type)

	companion object {
		private val gson = GsonBuilder()
			.registerTypeAdapter(Player::class.java, PlayerAdapter())
			.registerTypeAdapter(Technology::class.java, TechnologyAdapter())
			.serializeNulls()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create()
	}
}