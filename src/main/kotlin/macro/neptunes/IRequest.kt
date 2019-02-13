package macro.neptunes

import macro.neptunes.team.Team
import macro.neptunes.team.TeamTable

/**
 * Created by Macro303 on 2019-Feb-13.
 */
internal interface IRequest

data class PlayerRequest(val name: String?, val teamName: String?) : IRequest {
	fun getTeam(): Team? {
		return TeamTable.selectCreate(name = teamName ?: return null)
	}
}

data class TeamRequest(val name: String?, val players: List<String> = emptyList()) : IRequest