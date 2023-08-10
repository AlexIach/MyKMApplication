package com.alex.iachimov.mykmapplication.di

import com.alex.iachimov.mykmapplication.api.BreedsApi
import com.alex.iachimov.mykmapplication.database.createDriver
import com.alex.iachimov.mykmapplication.repository.BreedsLocalSource
import com.alex.iachimov.mykmapplication.repository.BreedsRemoteSource
import com.alex.iachimov.mykmapplication.repository.BreedsRepository
import com.alex.iachimov.mykmapplication.usecase.FetchBreedsUseCase
import com.alex.iachimov.mykmapplication.usecase.GetBreedsUseCase
import com.alex.iachimov.mykmapplication.usecase.ToggleFavouriteStateUseCase
import com.alex.iachimov.mykmapplication.util.getDispatcherProvider
import com.alex.iachimov.mykmmapplication.db.DogifyDatabase
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

private val utilityModule = module {
    factory { getDispatcherProvider() }
    single { DogifyDatabase(createDriver("dogify.db")) }
}

private val apiModule = module {
    factory { BreedsApi() }
}

private val repositoryModule = module {
    single { BreedsRepository() }

    factory { BreedsRemoteSource(get(), get()) }
    factory { BreedsLocalSource(get(), get()) }
}

private val usecaseModule = module {
    factory { GetBreedsUseCase() }
    factory { FetchBreedsUseCase() }
    factory { ToggleFavouriteStateUseCase() }
}

private val sharedModules = listOf(usecaseModule, repositoryModule, apiModule, utilityModule)

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(sharedModules)
}