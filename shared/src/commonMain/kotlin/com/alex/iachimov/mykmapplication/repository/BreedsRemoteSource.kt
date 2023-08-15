package com.alex.iachimov.mykmapplication.repository

import com.alex.iachimov.mykmapplication.api.BreedsApi
import com.alex.iachimov.mykmapplication.util.DispatcherProvider
import kotlinx.coroutines.withContext

internal class BreedsRemoteSource(
    private val api: BreedsApi,
    private val dispatcherProvider: DispatcherProvider // Platform specific
) {

    /**
     * GET list of breeds from API
     */
    suspend fun getBreeds() = withContext(dispatcherProvider.io) {
        api.getBreeds().breeds
    }

    /**
     * GET random link for breed's image
     */
    suspend fun getBreedImage(breed: String) = withContext(dispatcherProvider.io) {
        api.getRandomBreedImageFor(breed).breedImageUrl
    }
}