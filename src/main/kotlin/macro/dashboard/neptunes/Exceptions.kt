package macro.dashboard.neptunes

/**
 * Last Updated by Macro303 on 2019-May-14
 */
data class GeneralException(
	override val message: String = "Something happened check the logs, contact the devs, call the wizard"
) : RuntimeException()

data class BadRequestException(override val message: String) : RuntimeException()
data class UnauthorizedException(override val message: String) : RuntimeException()
data class NotFoundException(override val message: String) : RuntimeException()
data class ConflictException(override val message: String) : RuntimeException()
data class NotImplementedException(
	override val message: String = "This endpoint hasn't been implemented yet, feel free to make a pull request and add it."
) : RuntimeException()