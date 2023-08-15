package com.alex.iachimov.mykmapplication.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * KotlinX serialization annotations are used
 */
@Serializable
internal data class BreedImageResponse(
    @SerialName("message")
    val breedImageUrl: String
)