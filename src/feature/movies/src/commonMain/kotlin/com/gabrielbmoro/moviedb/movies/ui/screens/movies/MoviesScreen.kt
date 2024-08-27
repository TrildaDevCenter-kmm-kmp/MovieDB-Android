@file:Suppress("LongMethod")

package com.gabrielbmoro.moviedb.movies.ui.screens.movies

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gabrielbmoro.moviedb.SharedRes
import com.gabrielbmoro.moviedb.desingsystem.toolbars.AnimatedAppToolbar
import com.gabrielbmoro.moviedb.desingsystem.toolbars.AppToolbarTitle
import com.gabrielbmoro.moviedb.desingsystem.toolbars.MoviesTabIndex
import com.gabrielbmoro.moviedb.desingsystem.toolbars.NavigationBottomBar
import com.gabrielbmoro.moviedb.movies.ui.widgets.FilterMenu
import com.gabrielbmoro.moviedb.movies.ui.widgets.MoviesList
import com.gabrielbmoro.moviedb.platform.navigation.navigateToDetails
import com.gabrielbmoro.moviedb.platform.navigation.navigateToSearch
import com.gabrielbmoro.moviedb.platform.navigation.navigateToWishlist
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = koinViewModel(),
    navigator: NavHostController
) {
    val uiState = viewModel.uiState.collectAsState()

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val showTopBar by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0
        }
    }

    Scaffold(
        topBar = {
            AnimatedAppToolbar(
                appBar = {
                    AppToolbarTitle(
                        title = stringResource(SharedRes.strings.movies),
                        backEvent = null,
                        searchEvent = {
                            navigator.navigateToSearch("")
                        }
                    )
                },
                showTopBar = showTopBar,
            )
        },
        bottomBar = {
            NavigationBottomBar(
                currentTabIndex = MoviesTabIndex,
                onSelectMoviesTab = {
                    coroutineScope.launch {
                        lazyListState.scrollToItem(0)
                    }
                },
                onSelectFavoriteTab = {
                    navigator.navigateToWishlist()
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
                .fillMaxSize()
        ) {
            FilterMenu(
                modifier = Modifier.fillMaxWidth(),
                menuItems = uiState.value.menuItems,
                onClick = { filterMenuItem ->
                    viewModel.execute(
                        Intent.SelectFilterMenuItem(
                            menuItem = filterMenuItem
                        )
                    )
                }
            )

            MoviesList(
                movies = uiState.value.movies,
                onSelectMovie = { selectedMovie ->
                    navigator.navigateToDetails(selectedMovie.id)
                },
                onRequestMore = {
                    viewModel.execute(Intent.RequestMoreMovies)
                },
                modifier =
                Modifier
                    .fillMaxSize()
            )
        }
    }
}
