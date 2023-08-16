//
//  MainViewModel.swift
//  iosApp
//
//  Created by Alexandru IACHIMOV on 09.08.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import shared
import KMPNativeCoroutinesAsync
import Combine
import RxSwift
import KMPNativeCoroutinesRxSwift

/**
 *  Adding ObservableObject protocol  to allow internal properties to be observed
 */
class MainViewModel: ObservableObject {

    /**
     * Initialization of repository and use-cases using Koin
     */
    private let repository = BreedsRepository.init()
    private let getBreeds = GetBreedsUseCase.init()
    private let fetchBreeds = FetchBreedsUseCase.init()
    private let onToggleFavouriteState = ToggleFavouriteStateUseCase.init()

    /**
     *  Property marked as @Published within the ObservableObject will automatically notify SwiftUI to update the associated views whenever the property changes.
     *  It's similar to StateFlow we have in Android project
     */
    @Published
    private(set) var state = State.LOADING

    @Published
    var shouldFilterFavourites = false

    @Published
    private(set) var filteredBreeds: [Breed] = []

    @Published
    private var breeds: [Breed] = []

    /**
     * DisposeBag is container that holds onto disposable resources such as subscriptions. It helps manage the lifecycle of disposable objects,
     * It's similar to Disposable in Rxjava
     */
    private let disposeBag = DisposeBag()

    init() {
        /**
         * Create Swift observable using createObservable from KMPNativeCoroutinesRxSwift pod.
         * 1st parm - NativeFlow, which is auto-generated using KSP
         */
        createObservable(for: repository.breeds).subscribe(onNext: { breeds in
            // Executing breeds state update asynchronously on the main (UI) thread.
            DispatchQueue.main.async {
                self.breeds = breeds
            }
        }).disposed(by: disposeBag) // Adding disposeBag

        /**
         * Combine values from multiple publishers and emit a new value whenever any of the combined publishers emit a new value
         * It's similar to Flow combine() operator in Android
         * For Android devs. $ symbol is used to access property binding. It's used to interact with SwiftUI views. Having property binding allows us both read and write to
         * this property
         */
        $breeds.combineLatest($shouldFilterFavourites, { breeds, shouldFilterFavourites -> [Breed] in
            var result: [Breed] = []
            if(shouldFilterFavourites){
                result.append(contentsOf: breeds.filter{ $0.isFavourite })
            } else {
                result.append(contentsOf: breeds)
            }
            if(result.isEmpty){
                self.state = State.EMPTY
            } else {
                self.state = State.NORMAL
            }
            return result
        }).assign(to: &$filteredBreeds)

        getData()
    }

    /**
     * Get data from cache and handle UI states
     */
    func getData(){
        state = State.LOADING

        /**
         * Creates simple RxSwift Single to handle one-time event (handling Success and Error cases explicitely) from KMPNativeCoroutinesRxSwift pod.
         * 1st parm - NativeSuspend, which is auto-generated using KSP
         */
        createSingle(for: getBreeds.invoke()).subscribe(onSuccess: { _ in
            // Handling success case
            // Executing breeds state update asynchronously on the main (UI) thread.
            DispatchQueue.main.async {
                self.state = State.NORMAL
            }
        }, onFailure: { error in
            // Handling error case
            // Executing breeds state update asynchronously on the main (UI) thread.
            DispatchQueue.main.async {
                self.state = State.ERROR
            }
        }).disposed(by: disposeBag) // Adding disposeBag
    }

    func fetchData() {
        state = State.LOADING

        createSingle(for: fetchBreeds.invoke()).subscribe(onSuccess: { _ in
            DispatchQueue.main.async {
                self.state = State.NORMAL
            }
        }, onFailure: { error in
            DispatchQueue.main.async {
                self.state = State.ERROR
            }
        }).disposed(by: disposeBag)
    }

    func onFavouriteTapped(breed: Breed){
        createSingle(for: onToggleFavouriteState.invoke(breed: breed)).subscribe(onFailure: { error in
           // We're going ignoring the failure, as it will be represented by the stream of breeds
        }).disposed(by: disposeBag)
    }

    /**
     * Possible screen UI states
     */
    enum State {
        case NORMAL
        case LOADING
        case ERROR
        case EMPTY
    }
}
