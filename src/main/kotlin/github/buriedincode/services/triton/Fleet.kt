package github.buriedincode.services.triton

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Fleet(
  val ouid: Int,
  val uid: Int,
  val l: Int,
  val o: List<String>,
  @JsonNames("n") val name: String,
  val puid: Int,
  val w: Int,
  val y: Double,
  val x: Double,
  val st: Int,
  val lx: Double,
  val ly: Double,
)
