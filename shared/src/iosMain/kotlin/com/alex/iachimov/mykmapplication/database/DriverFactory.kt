package com.alex.iachimov.mykmapplication.database

import com.alex.iachimov.mykmmapplication.db.DogifyDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.core.scope.Scope

/**
 * 'actual' implementation of extension function createDriver for iOS platform
 * iOS specific driver is used - NativeSqliteDriver
 */
internal actual fun Scope.createDriver(databaseName: String): SqlDriver =
    NativeSqliteDriver(DogifyDatabase.Schema, databaseName)
