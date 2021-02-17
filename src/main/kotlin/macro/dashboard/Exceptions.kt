package macro.dashboard

/**
 * Created by Macro303 on 2021-Feb-16
 */
class NotImplementedException(message: String? = "This hasn't been implemented, feel free to make a pull request and add it."): Exception(message)
class ConflictException(message: String?) : Exception(message)