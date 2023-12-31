package com.alex.iachimov.mykmapplication.android

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alex.iachimov.mykmapplication.model.Breed
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainViewModel) {
    /**
     * Here, we're collecting different states from VM
     */
    val state by viewModel.state.collectAsState()
    val breeds by viewModel.breeds.collectAsState()
    val events by viewModel.events.collectAsState(Unit)
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val shouldFilterFavourites by viewModel.shouldFilterFavourites.collectAsState()

    /**
     * Create Compose related states
     */
    val scaffoldState = rememberScaffoldState()
    val snackbarCoroutineScope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState, content = { _ ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = viewModel::refresh
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Row(
                    Modifier
                        .wrapContentWidth(Alignment.End)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Filter favourites", Modifier.padding(top = 10.dp)
                    )
                    Switch(checked = shouldFilterFavourites,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onCheckedChange = { viewModel.onToggleFavouriteFilter() })
                }
                // Based in main UI state we display different UI
                when (state) {
                    // Displaying full screen loading
                    MainViewModel.State.LOADING -> {
                        Spacer(Modifier.weight(1f))
                        CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                        Spacer(Modifier.weight(1f))
                    }

                    MainViewModel.State.NORMAL -> {
                        // Displaying other Composable component which contains list of breeds
                        Breeds(
                            breeds = breeds, onFavouriteTapped = viewModel::onFavouriteTapped
                        )
                    }

                    MainViewModel.State.ERROR -> {
                        // Displaying full screen error message
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "Oops something went wrong...",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(Modifier.weight(1f))
                    }

                    MainViewModel.State.EMPTY -> {
                        // Displaying full screen empty message
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "Oops looks like there are no ${if (shouldFilterFavourites) "favourites" else "dogs"}",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(Modifier.weight(1f))
                    }
                }

                // Displaying SnackBar if Error event is received
                if (events == MainViewModel.Event.Error) {
                    snackbarCoroutineScope.launch {
                        scaffoldState.snackbarHostState.apply {
                            currentSnackbarData?.dismiss()
                            showSnackbar("Oops something went wrong...")
                        }
                    }
                }
            }
        }
    })
}

@Composable
fun Breeds(breeds: List<Breed>, onFavouriteTapped: (Breed) -> Unit = {}) {
    // Using vertical grid to display content
    LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
        items(breeds) {
            Column(Modifier.padding(8.dp)) {
                /**
                 * As image loading library, we're using Coil
                 * Dependency ref. --> build.gradle.kts --> androidApp module
                 */
                Image(
                    painter = rememberCoilPainter(request = it.imageUrl),
                    contentDescription = "${it.name}-image",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop

                )
                Row(Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = it.name, modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(if (it.isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Mark as favourite",
                        modifier = Modifier.clickable {
                            onFavouriteTapped(it)
                        }
                    )
                }
            }
        }
    })
}

@Preview
@Composable
fun BreedsPreview() {
    MaterialTheme {
        Surface {
            Breeds(breeds = (0 until 10).map {
                Breed(
                    name = "Breed $it", imageUrl = "", isFavourite = it % 2 == 0
                )
            })
        }
    }
}
