package com.golda.test.cats.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCat(
   @SerialName("breeds") val breeds: List<ResponseBreed>?,
   @SerialName("height") val height: Int?,
   @SerialName("id") val id: String?,
   @SerialName("url") val url: String?,
   @SerialName("width") val width: Int?
)