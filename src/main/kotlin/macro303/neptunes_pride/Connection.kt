package macro303.neptunes_pride

import com.google.gson.GsonBuilder
import javafx.concurrent.Task
import macro303.console.Console
import macro303.neptunes_pride.game.Game
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.*
import java.nio.charset.Charset

internal class Connection : Task<Game>() {

	@Throws(Exception::class)
	public override fun call(): Game? {
		refreshConfig()
		var connection: HttpURLConnection? = null
		try {
			connection = getConnection(address = "full")
			if (connection != null) {
				if (connection.responseCode == 200) {
					val response = readAll(connection.inputStream)
					val gson = GsonBuilder()
						.serializeNulls()
						.setPrettyPrinting()
						.disableHtmlEscaping()
						.create()
					return gson.fromJson(response, Game::class.java)
				} else
					cancel()
			} else
				cancel()
		} finally {
			if (connection != null)
				connection.disconnect()
		}
		cancel()
		return null
	}

	public override fun succeeded() {
		super.succeeded()
		Console.displayMessage(message = "Successful")
	}

	public override fun cancelled() {
		super.cancelled()
		Console.displayError(error = "Unable to Connect to API")
	}

	public override fun failed() {
		super.failed()
		Console.displayError(error = "Unable to Connect to API: ${exception.localizedMessage}")
	}

	private fun getApiAddress(): String {
		return "$apiAddress${config.gameID}/"
	}

	@Throws(IOException::class)
	private fun getConnection(address: String): HttpURLConnection? {
		Console.displayMessage(message = "API Address: ${getApiAddress()}$address")
		val url = URL(getApiAddress() + address)
		var connection: HttpURLConnection? = if (config.proxyHostname != null && config.proxyPort != null) {
			val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(config.proxyHostname!!, config.proxyPort!!))
			url.openConnection(proxy) as HttpURLConnection
		} else {
			url.openConnection() as HttpURLConnection
		}
		connection!!.requestMethod = "GET"
		try {
			connection.connect()
			Console.displayMessage("Connected Successfully")
		} catch (ce: ConnectException) {
			connection = null
			Console.displayMessage("Connected Unsuccessfully: ${ce.localizedMessage}")
		}

		return connection
	}

	@Throws(IOException::class)
	private fun readAll(stream: InputStream): String {
		val reader = BufferedReader(InputStreamReader(stream, Charset.forName("UTF-8")))
		val builder = StringBuilder()
		var cp: Int = reader.read()
		while (cp != -1) {
			builder.append(cp.toChar())
			cp = reader.read()
		}
		return builder.toString()
	}

	companion object {
		lateinit var config: Config
		private const val apiAddress = "http://nptriton.cqproject.net/game/"

		private fun refreshConfig() {
			config = Config.loadConfig()
		}
	}
}