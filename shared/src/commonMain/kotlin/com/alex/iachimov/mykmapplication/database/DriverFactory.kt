package com.alex.iachimov.mykmapplication.database

import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.scope.Scope

/**
 * Extension function for Scope class from Koin library.
 * Useful when we need to access Android context
 * Added 'expect' declaration to have platform specific 'actual' implementation
 */
internal expect fun Scope.createDriver(databaseName: String): SqlDriver