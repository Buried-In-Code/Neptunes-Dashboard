package github.buriedincode.services.triton

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Technology(
  val level: Int,
  val sv: Double? = null,
  val value: Double,
  val research: Int? = null,
  val bv: Double? = null,
  val brr: Double? = null,
)
