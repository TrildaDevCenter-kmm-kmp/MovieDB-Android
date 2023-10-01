package com.gabrielbmoro.moviedb.details.ui.screens.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gabrielbmoro.moviedb.core.ui.widgets.AppToolbar
import com.gabrielbmoro.moviedb.core.ui.widgets.BubbleLoader
import com.gabrielbmoro.moviedb.core.ui.widgets.MovieImage
import com.gabrielbmoro.moviedb.details.ui.screens.fullscreen.FullScreenActivity
import com.gabrielbmoro.moviedb.details.ui.widgets.ErrorMessage
import com.gabrielbmoro.moviedb.details.ui.widgets.MovieDetailDescription
import com.gabrielbmoro.moviedb.details.ui.widgets.MovieDetailIndicator
import com.gabrielbmoro.moviedb.details.ui.widgets.VideoPlayer
import com.gabrielbmoro.moviedb.feature.details.R
import com.gabrielbmoro.moviedb.repository.model.Movie

@Composable
fun DetailsScreen(
    viewModel: DetailsScreenViewModel = hiltViewModel(),
    movie: Movie,
    onBackEvent: () -> Unit
) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val atTop = scrollState.value == 0

    DetailsScreenMain(
        atTop = atTop,
        uiState = uiState,
        scrollState = scrollState,
        onFavoriteMovie = {
            viewModel.isToFavoriteOrUnFavorite(it)
        },
        onBackEvent = onBackEvent
    )

    LaunchedEffect(
        key1 = Unit,
        block = {
            viewModel.setup(movie)
        }
    )
}

@Composable
private fun DetailsScreenMain(
    atTop: Boolean,
    uiState: DetailsUIState,
    scrollState: ScrollState,
    onFavoriteMovie: ((Boolean) -> Unit),
    onBackEvent: (() -> Unit)
) {
    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = atTop,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                AppToolbar(
                    title = uiState.movieTitle,
                    backEvent = onBackEvent
                )
            }
        }
    ) {
        val modifier = Modifier
            .padding(top = it.calculateTopPadding())
            .fillMaxSize()

        when (uiState) {
            is DetailsUIState.SuccessData -> DetailsScreenSuccessInfo(
                uiState = uiState,
                modifier = Modifier
                    .then(modifier)
                    .verticalScroll(scrollState),
                onFavoriteMovie = onFavoriteMovie
            )

            is DetailsUIState.Error ->
                Box(modifier = modifier) {
                    ErrorMessage(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            is DetailsUIState.Loading -> {
                Box(modifier = modifier) {
                    BubbleLoader(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailsScreenSuccessInfo(
    uiState: DetailsUIState.SuccessData,
    modifier: Modifier = Modifier,
    onFavoriteMovie: (Boolean) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .height(280.dp)
                .fillMaxWidth()
        ) {
            if (uiState.videoId != null) {
                VideoPlayer(
                    videoId = uiState.videoId,
                    onFullScreenEvent = { videoId ->
                        context.startActivity(
                            FullScreenActivity.launchIntent(context, videoId)
                        )
                    },
                    shouldStartMuted = true,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxSize()
                )
            } else {
                MovieImage(
                    imageUrl = uiState.imageUrl,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxSize(),
                    contentDescription = stringResource(id = R.string.poster)
                )
            }
        }

        MovieDetailIndicator(
            isFavorite = uiState.isFavorite,
            votesAverage = uiState.movieVotesAverage,
            onFavoriteMovie = onFavoriteMovie,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        MovieDetailDescription(
            titleRes = R.string.overview,
            content = {
                Text(
                    text = uiState.movieOverview,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        MovieDetailDescription(
            titleRes = R.string.popularity,
            content = {
                Text(
                    text = uiState.moviePopularity.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        MovieDetailDescription(
            titleRes = R.string.language,
            content = {
                Text(
                    text = uiState.movieLanguage,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        uiState.genres?.let {
            MovieDetailDescription(
                titleRes = R.string.genres,
                content = {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        uiState.tagLine?.let {
            MovieDetailDescription(
                titleRes = R.string.tagline,
                content = {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        uiState.productionCompanies?.let {
            MovieDetailDescription(
                titleRes = R.string.production_companies,
                content = {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        uiState.homepage?.let {
            MovieDetailDescription(
                titleRes = R.string.homepage,
                content = {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(
            modifier = Modifier
                .height(240.dp)
                .padding(horizontal = 16.dp)
        )
    }
}
