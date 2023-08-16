import SwiftUI
import shared

struct ContentView: View {
    /**
     * Create binding between SwiftUI and an instance of a class that conforms to the ObservableObject protocol --> MainViewModel
     */
    @ObservedObject private var viewModel: MainViewModel

    /**
     * Koin initialization. Init everything we provided in KoinModule
     */
    init() {
        KoinModuleKt.doInitKoin()
        viewModel = MainViewModel.init()
    }

    // Create property that conforms View protocol
    var body: some View {
        // Arrange chikds vertically
        VStack {
            Toggle("Filter favourites", isOn: $viewModel.shouldFilterFavourites)
                .padding(16)

            Button("Refresh breeds", action: {
              viewModel.fetchData()} )
                .frame(alignment: .center)
                .padding(.bottom, 16)

            // Handling different UI states
            // Arrange childs in a stack, overlaying them on top of each other
            ZStack {
                switch viewModel.state {
                case MainViewModel.State.LOADING:
                    ProgressView().frame(alignment:.center)
                case MainViewModel.State.NORMAL:
                    BreedsGridUIView(
                        breeds: viewModel.filteredBreeds,
                        onFavouriteTapped: viewModel.onFavouriteTapped)

                case MainViewModel.State.EMPTY:
                    Text("Ooops looks like there are no breeds")
                        .frame(alignment: .center)
                        .font(.headline)
                case MainViewModel.State.ERROR:
                   Text("Ooops something went wrong...")
                        .frame(alignment: .center)
                        .font(.headline)
                }
            }
        }
    }
}
