package com.alex.iachimov.mykmapplication.android

import android.app.Application
import com.alex.iachimov.mykmapplication.di.initKoin
import org.koin.android.ext.koin.androidContext

class DogifyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@DogifyApplication)
            modules(viewModelModule)
        }
    }
}