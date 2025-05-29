package github.buriedincode.models

interface IJson {
  fun toJson(showAll: Boolean = false): Map<String, Any?>
}
