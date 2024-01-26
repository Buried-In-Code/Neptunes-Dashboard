package github.buriedincode.dashboard.services.triton

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Star(
    val c: Double,
    val e: Int,
    val uid: Int,
    val i: Int,
    val s: Int,
    @JsonNames("n")
    val name: String,
    val puid: Int,
    val r: Int,
    val ga: Int,
    val v: Int,
    val y: Double,
    val x: Double,
    val nr: Int,
    val st: Int,
)
