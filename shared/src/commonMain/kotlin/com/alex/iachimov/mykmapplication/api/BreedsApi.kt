package com.alex.iachimov.mykmapplication.api

import com.alex.iachimov.mykmapplication.api.model.BreedImageResponse
import com.alex.iachimov.mykmapplication.api.model.BreedsResponse
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Ktor Networking Api for getting information about a Breed entity
 */
internal class BreedsApi : KtorApi() {

    suspend fun getBreeds(): BreedsResponse = client.get {
        contentType(ContentType.Application.Json)
        apiUrl("breeds/list")
    }.body()

    suspend fun getRandomBreedImageFor(breed: String): BreedImageResponse = client.get {
        contentType(ContentType.Application.Json)
        apiUrl("breed/$breed/images/random")
    }.body()
}