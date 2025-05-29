package github.buriedincode.services.triton

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class) @Serializable data class Response(val scanningData: ScanData)
