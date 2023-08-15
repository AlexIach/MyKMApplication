package com.alex.iachimov.mykmapplication.android

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// Setup Android specific Koin module
val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), get(), get()) }
}