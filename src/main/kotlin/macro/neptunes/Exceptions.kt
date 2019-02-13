package macro.neptunes

import io.ktor.http.ContentType
import macro.neptunes.ErrorMessage
import org.apache.logging.log4j.Level

/**
 * Created by Macro303 on 2019-Feb-13.
 */
data class InvalidContentTypeException(val value: ContentType) : RuntimeException(){
	override fun toString(): String {
		return "To access this endpoint the header Content-Type needs to be ${ContentType.Application.Json}, you supplied $value"
	}
}

data class DataExistsException(val field: String, val value: Any?): RuntimeException(){
	override fun toString(): String {
		return "$field should be unique, $value already exists in the database"
	}
}

data class NotImplementedException(val ignored: Nothing? = null): RuntimeException(){
	override fun toString(): String {
		return "This endpoint hasn't been implemented yet, feel free to make a pull request and add it."
	}
}