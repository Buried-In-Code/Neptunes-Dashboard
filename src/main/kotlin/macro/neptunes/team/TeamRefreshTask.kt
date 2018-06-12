package macro.neptunes.team

import macro.neptunes.RefreshTask
import macro.neptunes.player.Player
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.util.*
import kotlin.collections.ArrayList

class TeamRefreshTask(model: macro.neptunes.Model) : RefreshTask<ArrayList<Team>?>(model = model) {

	override fun call(): ArrayList<Team>? {
		var connection: HttpURLConnection? = null
		try {
			connection = getConnection()
			if (connection != null) {
				println("Response Code: ${connection.responseCode}")
				if (connection.responseCode == 200) {
					val playerWrapper =
						gson.fromJson<HashMap<String, Player>>(InputStreamReader(connection.inputStream))
					val teams = ArrayList<Team>()
					for (player in playerWrapper.values) {
						var team = Team()
						for (temp in teams) {
							if (temp.name.equals(player.team, ignoreCase = true))
								team = temp
						}
						team.addMember(player)
						if (!teams.contains(team))
							teams.add(team)
					}
					return teams
				}
			}
		} finally {
			connection?.disconnect()
		}
		cancel()
		return null
	}
}