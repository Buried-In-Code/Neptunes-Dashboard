package macro.dashboard.neptunes

/**
 * Created by Macro303 on 2019-Feb-13.
 */
data class InvalidBodyException(val field: String) : RuntimeException() {
	override fun toString(): String {
		return "$field is required as not null"
	}
}

data class DataNotFoundException(val errorMessage: String) : RuntimeException() {
	constructor(
		type: String,
		field: String,
		value: Any?
	) : this(errorMessage = "No $type was found with the $field: $value in the database")
}

data class GeneralException(val ignored: Nothing? = null) : RuntimeException() {
	override fun toString(): String {
		return "Something happened check the logs, contact the devs, call the wizard"
	}
}

data class UnknownException(override val message: String) : RuntimeException()

data class BadRequestException(override val message: String) : RuntimeException()
data class UnauthorizedException(override val message: String) : RuntimeException()
data class NotFoundException(override val message: String) : RuntimeException()
data class ConflictException(override val message: String) : RuntimeException()
data class NotImplementedException(
	override val message: String = "This endpoint hasn't been implemented yet, feel free to make a pull request and add it."
) : RuntimeException()