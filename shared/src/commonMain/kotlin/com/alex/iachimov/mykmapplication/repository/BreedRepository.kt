package com.alex.iachimov.mykmapplication.repository

import com.alex.iachimov.mykmapplication.model.Breed
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Used to combine multiple data sources.
 * KoinComponent allows property injection using delegate - inject()
 * KMP-NativeCoroutine library usages
 */
class BreedsRepository : KoinComponent {
    // Inject remote source
    private val remoteSource: BreedsRemoteSource by inject()

    // Inject local source
    private val localSource: BreedsLocalSource by inject()

    /**
     * NativeCoroutines annotation helps to generate code that can be used with RxSwift wrapper functions:
     * AsyncStreams, Publishers, Observables. It's based on KSP code-generation. It's generated for each target (X64, Arm64, e.t.c)
     * Flow -> NativeFlow(callback functions)
     */
    @NativeCoroutines
    val breeds = localSource.breeds // Get reactive stream from local source for further usages

    /**
     * Suspend -> NativeSuspend(callback functions)
     */
    @NativeCoroutines
    internal suspend fun get() = with(localSource.selectAll()) {
        if (isNullOrEmpty()) {
            return@with fetch()
        } else {
            this
        }
    }

    /**
     * supervisorScope{...} is used to avoid main coroutine cancellation when child coroutine failed for some reasons
     */
    @NativeCoroutines
    internal suspend fun fetch() = supervisorScope {
        remoteSource.getBreeds().map {
            async {
                Breed(
                    name = it,
                    imageUrl = remoteSource.getBreedImage(it)
                )
            } // async{...} to start parallel operations to get images
        }.awaitAll().also {// wait for all results
            localSource.clear()
            it.map { async { localSource.insert(it) } }
                .awaitAll() // async{...} to start parallel operations insert entities in DB
        }
    }

    @NativeCoroutines
    suspend fun update(breed: Breed) = localSource.update(breed)
}