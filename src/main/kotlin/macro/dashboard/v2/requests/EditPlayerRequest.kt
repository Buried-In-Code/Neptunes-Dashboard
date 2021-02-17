package macro.dashboard.v2.requests

import macro.dashboard.v2.IRequest

/**
 * Created by Macro303 on 2021-Feb-18
 */
data class EditPlayerRequest(var team: String?): IRequest {
	override fun validate(): Boolean {
		if (team.isNullOrBlank())
			team = null
		return true
	}
}