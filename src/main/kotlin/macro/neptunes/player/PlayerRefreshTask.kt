package macro.neptunes.player

import macro.neptunes.RefreshTask
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.util.*
import kotlin.collections.ArrayList

class PlayerRefreshTask(model: macro.neptunes.Model) : RefreshTask<ArrayList<Player>?>(model = model) {

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