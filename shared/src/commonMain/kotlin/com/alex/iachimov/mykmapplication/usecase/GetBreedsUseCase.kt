package com.alex.iachimov.mykmapplication.usecase

import com.alex.iachimov.mykmapplication.model.Breed
import com.alex.iachimov.mykmapplication.repository.BreedsRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetBreedsUseCase : KoinComponent {

    private val breedsRepository: BreedsRepository by inject()

    @NativeCoroutines
    suspend operator fun invoke(): List<Breed> = breedsRepository.get()
}