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
    private val dispatcherProvider: DispatcherProvider // Platform specific
) {
    /**
     * Get DAO based on auto-generated queries by SQLDelight plugin.
     * ref. commonMain/sqldelight/../db.Breeds.sq
     */
    private val dao = database.breedsQueries

    /**
     * Subscribe to DB changes in reactive way.
     * Convert data we have in DB into Flow and then Flow converts data into reactive stream,
     * so we could follow and react to any changes in real-time.
     */
    val breeds = dao.selectAll().asFlow().mapToList()
        .map { breeds -> breeds.map { Breed(it.name, it.imageUrl, it.isFavourite ?: false) } }

    /**
     * Select ALL breed entities from BREED table in DB using I/O dispatcher
     */
    suspend fun selectAll() = withContext(dispatcherProvider.io) {
        dao.selectAll { name, imageUrl, isFavourite -> Breed(name, imageUrl, isFavourite ?: false) }
            .executeAsList()
    }

    /**
     * Insert Breed entity into BREED table using I/O dispatcher
     */
    suspend fun insert(breed: Breed) = withContext(dispatcherProvider.io) {
        dao.insert(breed.name, breed.imageUrl, breed.isFavourite)
    }

    /**
     * Update breed entity in BREED table using I/O dispatcher
     */
    suspend fun update(breed: Breed) = withContext(dispatcherProvider.io) {
        dao.update(breed.imageUrl, breed.isFavourite, breed.name)
    }

    /**
     * Clear BREED table
     */
    suspend fun clear() = withContext(dispatcherProvider.io) {
        dao.clear()
    }
}