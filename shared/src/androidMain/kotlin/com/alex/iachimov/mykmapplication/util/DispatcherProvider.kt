package com.alex.iachimov.mykmapplication.util

import kotlinx.coroutines.Dispatchers

/**
 * Adding 'actual' declaration for getDispatcherProvider() that returns actual AndroidDispatcher implementation
 */
internal actual fun getDispatcherProvider(): DispatcherProvider = AndroidDispatcherProvider()

private class AndroidDispatcherProvider: DispatcherProvider{
    override val main = Dispatchers.Main
    override val io = Dispatchers.IO
    override val unconfined = Dispatchers.Unconfined
}