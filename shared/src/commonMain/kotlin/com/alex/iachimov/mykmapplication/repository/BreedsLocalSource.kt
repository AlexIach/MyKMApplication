package com.alex.iachimov.mykmapplication.repository

import com.alex.iachimov.mykmapplication.model.Breed
import com.alex.iachimov.mykmapplication.util.DispatcherProvider
import com.alex.iachimov.mykmmapplication.db.DogifyDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class BreedsLocalSource(
    database: DogifyDatabase,
    private val dispatcherProvider: DispatcherProvider
) {
    private val dao = database.breedsQueries

    val breeds = dao.selectAll().asFlow().mapToList()
        .map { breeds -> breeds.map { Breed(it.name, it.imageUrl, it.isFavourite ?: false) } }

    suspend fun selectAll() = withContext(dispatcherProvider.io) {
        dao.selectAll { name, imageUrl, isFavourite -> Breed(name, imageUrl, isFavourite ?: false) }
            .executeAsList()
    }

    suspend fun insert(breed: Breed) = withContext(dispatcherProvider.io) {
        dao.insert(breed.name, breed.imageUrl, breed.isFavourite)
    }

    suspend fun update(breed: Breed) = withContext(dispatcherProvider.io) {
        dao.update(breed.imageUrl, breed.isFavourite, breed.name)
    }

    suspend fun clear() = withContext(dispatcherProvider.io) {
        dao.clear()
    }
}