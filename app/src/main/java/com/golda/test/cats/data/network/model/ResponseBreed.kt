package com.golda.test.cats.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseBreed(
    @SerialName("alt_names") val altNames: String?,
    @SerialName("name") val name: String?
)