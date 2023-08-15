package com.alex.iachimov.mykmapplication.api

import com.alex.iachimov.mykmapplication.api.model.BreedImageResponse
import com.alex.iachimov.mykmapplication.api.model.BreedsResponse
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Ktor Networking Api for getting information about a Breed entity
 */
internal class BreedsApi : KtorApi() {

    /**
     * HTTP GET request to get list of available breeds
     */
    suspend fun getBreeds(): BreedsResponse =
        client.get { // Executes an HttpClient's GET request with the parameters configured in block.
            contentType(ContentType.Application.Json) // Set Content-Type header
            apiUrl("breeds/list") // Configure API details
        }.body() // Receive response payload

    /**
     * HTTP GET request to get link to a random image for specified breed
     */
    suspend fun getRandomBreedImageFor(breed: String): BreedImageResponse =
        client.get { // Executes an HttpClient's GET request with the parameters configured in block.
            contentType(ContentType.Application.Json) // Set Content-Type header
            apiUrl("breed/$breed/images/random") // Configure API details
        }.body() // Receive response payload
}