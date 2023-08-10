package com.alex.iachimov.mykmapplication.repository

import com.alex.iachimov.mykmapplication.model.Breed
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BreedsRepository : KoinComponent {
    private val remoteSource: BreedsRemoteSource by inject()
    private val localSource: BreedsLocalSource by inject()

    @NativeCoroutines
    val breeds = localSource.breeds

    @NativeCoroutines
    internal suspend fun get() = with(localSource.selectAll()) {
        if (isNullOrEmpty()) {
            return@with fetch()
        } else {
            this
        }
    }

    @NativeCoroutines
    internal suspend fun fetch() = supervisorScope {
        remoteSource.getBreeds().map {
            async { Breed(name = it, imageUrl = remoteSource.getBreedImage(it)) }
        }.awaitAll().also {
            localSource.clear()
            it.map { async { localSource.insert(it) } }.awaitAll()
        }
    }

    @NativeCoroutines
    suspend fun update(breed: Breed) = localSource.update(breed)
}