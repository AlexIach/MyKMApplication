package com.alex.iachimov.mykmapplication.database

import com.alex.iachimov.mykmmapplication.db.DogifyDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

/**
 * 'actual' implementation of extension function createDriver for Android platform
 * Android specific driver is used - AndroidSqliteDriver
 * Here we have the access to Android context
 */
internal actual fun Scope.createDriver(databaseName: String): SqlDriver =
    AndroidSqliteDriver(DogifyDatabase.Schema, androidContext(), databaseName)