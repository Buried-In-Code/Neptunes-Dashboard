package macro303.neptunes

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import javafx.concurrent.Task
import macro303.neptunes.game.Game
import macro303.neptunes.game.GameAdapter
import macro303.neptunes.player.Player
import macro303.neptunes.player.PlayerAdapter
import macro303.neptunes.technology.Technology
import macro303.neptunes.technology.TechnologyAdapter
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL

abstract class RefreshTask<V>(private val model: Model) : Task<V>() {
	protected val gson = GsonBuilder()
		.registerTypeAdapter(Game::class.java, GameAdapter())
		.registerTypeAdapter(Player::class.java, PlayerAdapter())
		.registerTypeAdapter(Technology::class.java, TechnologyAdapter())
		.serializeNulls()
		.setPrettyPrinting()
		.disableHtmlEscaping()
		.create()

	protected fun getConnection(): HttpURLConnection? {
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
}