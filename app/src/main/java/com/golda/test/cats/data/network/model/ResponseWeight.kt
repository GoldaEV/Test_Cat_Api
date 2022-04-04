package com.golda.test.cats.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseWeight(
    @SerialName("imperial") val imperial: String?,
    @SerialName("metric") val metric: String?
)