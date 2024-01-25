package github.buriedincode.dashboard.models

interface IJson {
    fun toJson(showAll: Boolean = false): Map<String, Any?>
}
