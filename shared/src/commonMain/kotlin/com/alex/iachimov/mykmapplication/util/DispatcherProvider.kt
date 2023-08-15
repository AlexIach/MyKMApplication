package com.alex.iachimov.mykmapplication.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * A Dispatcher abstraction in order to ease testing coroutines.
 * We need this abstraction to provide different implementation for each platform (Android and iOS)
 * Kotlin Native does not support I/O dispatcher and we have to use Default one instead
 */
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}

/**
 * 'expected' declaration makes us add 'actual' declaration for each platform
 * (iosMain/kotlin/util/DispatcherProvider and androidMain/kotlin/util/DispatcherProvider)
 */
internal expect fun getDispatcherProvider(): DispatcherProvider