package com.alex.iachimov.mykmapplication.usecase

import com.alex.iachimov.mykmapplication.model.Breed
import com.alex.iachimov.mykmapplication.repository.BreedsRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Used to trigger scenario when user wants to get list of breeds from cache (DB)
 */
class GetBreedsUseCase : KoinComponent {

    // Injecting BreedsRepository
    private val breedsRepository: BreedsRepository by inject()

    /**
     * Adding keyword 'operator' to method called "invoke" we can call instance of ToggleFavouriteStateUseCase as function
     */
    @NativeCoroutines
    suspend operator fun invoke(): List<Breed> = breedsRepository.get()
}