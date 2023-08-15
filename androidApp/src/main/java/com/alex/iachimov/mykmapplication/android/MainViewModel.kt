package com.alex.iachimov.mykmapplication.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.iachimov.mykmapplication.model.Breed
import com.alex.iachimov.mykmapplication.repository.BreedsRepository
import com.alex.iachimov.mykmapplication.usecase.FetchBreedsUseCase
import com.alex.iachimov.mykmapplication.usecase.GetBreedsUseCase
import com.alex.iachimov.mykmapplication.usecase.ToggleFavouriteStateUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel is designed to handle different UI states like save and update and handle user and system
 * actions
 */
class MainViewModel(
    breedsRepository: BreedsRepository,
    private val getBreeds: GetBreedsUseCase,
    private val fetchBreeds: FetchBreedsUseCase,
    private val onToggleFavouriteState: ToggleFavouriteStateUseCase
) : ViewModel() {

    /**
     * Create state flow for all possible screen states: LOADING, ERROR, EMPTY, NORMAL
     */
    private val _state = MutableStateFlow(State.LOADING)
    val state: StateFlow<State> = _state

    /**
     * Create state flow for refresh action
     */
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    /**
     * Create state flow for filter action
     */
    private val _shouldFilterFavourites = MutableStateFlow(false)
    val shouldFilterFavourites: StateFlow<Boolean> = _shouldFilterFavourites

    /**
     * Create shared flow to handle error event
     */
    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events

    /**
     * Define shared combined flow: breeds + shouldFilterFavourites = single flow.
     * We can emit most recent values to multiple collectors (subscribers)
     * Should emit values whenever any of the combined flows emit a new value.
     */
    val breeds =
        breedsRepository.breeds.combine(shouldFilterFavourites) { breeds, shouldFilterFavourites ->
            if (shouldFilterFavourites) {
                breeds.filter { it.isFavourite }
            } else {
                breeds
            }.also {
                _state.value = if (it.isEmpty()) State.EMPTY else State.NORMAL
            }
        }.stateIn(
            viewModelScope, // Specify CoroutineScope in which the stateful flow will be managed.
            SharingStarted.WhileSubscribed(), // Specify strategy that determines when the stateful flow starts and stops sharing its state among collectors.
            emptyList() // Specify initialValue that will be emitted immediately to new collectors when they start observing the stateful flow.
        )

    init {
        loadData()
    }

    private fun loadData(isForceRefresh: Boolean = false) {
        // Check what use-case we want to invoke
        val getData: suspend () -> List<Breed> =
            { if (isForceRefresh) fetchBreeds() else getBreeds() }

        /**
         * Updating UI state or isRefreshing state, depending on isForceRefresh bool flag
         */
        if (isForceRefresh) {
            _isRefreshing.value = true
        } else {
            _state.value = State.LOADING
        }

        /**
         * Call suspend function inside viewModel scope and updating UI state and refreshing state
         */
        viewModelScope.launch {
            _state.value = try {
                getData()
                State.NORMAL
            } catch (e: Exception) {
                State.ERROR
            }
            _isRefreshing.value = false
        }
    }

    /**
     * Refresh method to be called as lambda function onRefresh: () -> Unit in SwipeRefresh composable
     */
    fun refresh() {
        loadData(true)
    }

    /**
     * Method to be called when user changes switch state onCheckedChange: ((Boolean) -> Unit)?
     */
    fun onToggleFavouriteFilter() {
        _shouldFilterFavourites.value = !shouldFilterFavourites.value
    }

    /**
     * Method to be called when user taps on corresponding like icon.
     * Method launches coroutine in viewModelScope to update breed in DB
     * If something went wrong we should emit Error event.
     * We should use SharedState flow for this event emission to support backpressure
     */
    fun onFavouriteTapped(breed: Breed) {
        viewModelScope.launch {
            try {
                onToggleFavouriteState(breed)
            } catch (e: Exception) {
                _events.emit(Event.Error)
            }
        }
    }

    /**
     * Possible screen UI states
     */
    enum class State {
        LOADING,
        NORMAL,
        ERROR,
        EMPTY
    }

    enum class Event {
        Error
    }
}