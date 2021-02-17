package macro.dashboard.v2.requests

import io.ktor.features.BadRequestException
import macro.dashboard.v2.IRequest

/**
 * Created by Macro303 on 2019-Dec-19.
 */
data class NewGameRequest(var apiCode: String, var tickRate: Int): IRequest {
	override fun validate(): Boolean {
		if (tickRate <= 0)
			throw BadRequestException("Invalid Tick Rate: `$tickRate`")
		return true
	}
}