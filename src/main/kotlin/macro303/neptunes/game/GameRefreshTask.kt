package macro303.neptunes.game

import macro303.neptunes.Model
import macro303.neptunes.RefreshTask
import java.io.InputStreamReader
import java.net.HttpURLConnection

class GameRefreshTask(model: Model) : RefreshTask<Game?>(model = model) {

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