package macro.neptunes

import com.google.gson.JsonSyntaxException
import macro.neptunes.team.Team
import macro.neptunes.team.TeamTable

/**
 * Created by Macro303 on 2019-Feb-13.
 */
internal interface IRequest

data class PlayerRequest(val name: String?, val teamName: String?) : IRequest {
	fun getTeam(): Team? {
		teamName ?: return null
		return TeamTable.selectCreate(teamName)
	}

	companion object {
		@Throws(JsonSyntaxException::class)
		fun String.JsonToRequest(): PlayerRequest? {
			if (this.isBlank()) return null
			return Util.GSON.fromJson(this, PlayerRequest::class.java)
		}
	}
}

data class TeamRequest(val name: String?, val players: List<String>?) : IRequest {

	companion object {
		@Throws(JsonSyntaxException::class)
		fun String.JsonToRequest(): TeamRequest? {
			if (this.isBlank()) return null
			return Util.GSON.fromJson(this, TeamRequest::class.java)
		}
	}
}