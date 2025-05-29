package github.buriedincode.services.triton

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Player(
  val researching: String? = null,
  val uid: Int,
  val ai: Int,
  val huid: Int,
  val totalFleets: Int,
  val ready: Int,
  val karmaToGive: Int,
  val war: Map<String, Int> = emptyMap(),
  val totalIndustry: Int,
  val totalStars: Int,
  val regard: Int,
  val conceded: Int,
  val totalScience: Int,
  val starsAbandoned: Int? = null,
  val cash: Int? = null,
  val totalStrength: Int,
  @JsonNames("alias") val username: String,
  val tech: Map<String, Technology>,
  val avatar: Int,
  val researching_next: String? = null,
  val totalEconomy: Int,
  val countdownToWar: Map<String, Int> = emptyMap(),
  val missedTurns: Int,
) {
  val isActive: Boolean
    get() = this.conceded == 0
}
