package com.alex.iachimov.mykmapplication.util

import kotlinx.coroutines.Dispatchers

/**
 * Adding 'actual' declaration for getDispatcherProvider() that returns actual iOSDispatcher implementation
 */
internal actual fun getDispatcherProvider(): DispatcherProvider = IosDispatcherProvider()

private class IosDispatcherProvider : DispatcherProvider {
    override val main = Dispatchers.Main
    override val io = Dispatchers.Default
    override val unconfined = Dispatchers.Unconfined
}