package com.gabrielbmoro.moviedb.movies.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielbmoro.moviedb.domain.entities.Movie
import com.gabrielbmoro.moviedb.domain.usecases.GetNowPlayingMoviesUseCase
import com.gabrielbmoro.moviedb.domain.usecases.GetPopularMoviesUseCase
import com.gabrielbmoro.moviedb.domain.usecases.GetTopRatedMoviesUseCase
import com.gabrielbmoro.moviedb.domain.usecases.GetUpcomingMoviesUseCase
import com.gabrielbmoro.moviedb.movies.ui.widgets.FilterMenuItem
import com.gabrielbmoro.moviedb.movies.ui.widgets.FilterType
import com.gabrielbmoro.moviedb.movies.ui.widgets.MovieCardInfo
import com.gabrielbmoro.moviedb.platform.ViewModelMvi
import com.gabrielbmoro.moviedb.platform.paging.PagingController
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel(), ViewModelMvi<Intent> {

    private val _uiState = MutableStateFlow(this.defaultEmptyState())
    val uiState = _uiState.stateIn(viewModelScope, SharingStarted.Eagerly, _uiState.value)

    private val moviesPageController: PagingController<Movie> = PagingController(
        coroutineScope = viewModelScope,
        ioCoroutineDispatcher = ioDispatcher,
        requestMore = { pageIndex ->
            when (_uiState.value.selectedFilterMenu) {
                FilterType.NowPlaying -> {
                    getNowPlayingMoviesUseCase.execute(GetNowPlayingMoviesUseCase.Params(pageIndex))
                }

                FilterType.TopRated -> {
                    getTopRatedMoviesUseCase.execute(GetTopRatedMoviesUseCase.Params(pageIndex))
                }

                FilterType.Popular -> {
                    getPopularMoviesUseCase.execute(GetPopularMoviesUseCase.Params(pageIndex))
                }

                FilterType.UpComing -> {
                    getUpcomingMoviesUseCase.execute(GetUpcomingMoviesUseCase.Params(pageIndex))
                }
            }
        }
    )

    init {
        execute(Intent.Setup)
    }

    override fun execute(intent: Intent) {
        when (intent) {
            is Intent.RequestMoreMovies -> {
                viewModelScope.launch(ioDispatcher) {
                    processRequestMoreForMoreMoviesIntent()
                }
            }

            Intent.Setup -> {
                moviesPageController.reset()

                viewModelScope.launch(ioDispatcher) {
                    _uiState.update {
                        it.copy(
                            movieCardInfos = moviesPageController.onRequestMore().map(
                                ::toMovieCardInfo
                            ).toPersistentList(),
                            isLoading = false,
                        )
                    }
                }
            }

            is Intent.SelectFilterMenuItem -> {
                _uiState.update {
                    it.copy(
                        selectedFilterMenu = intent.menuItem.type,
                        movieCardInfos = persistentListOf(),
                        isLoading = true,
                        menuItems = it.menuItems.updateAccordingToFilterType(
                            newFilterType = intent.menuItem.type
                        )
                    )
                }

                execute(Intent.Setup)
            }
        }
    }

    private fun toMovieCardInfo(movie: Movie) = MovieCardInfo(
        movieId = movie.id,
        movieTitle = movie.title,
        moviePosterUrl = movie.posterImageUrl ?: ""
    )

    private suspend fun processRequestMoreForMoreMoviesIntent() {
        val movies = moviesPageController.onRequestMore().map(::toMovieCardInfo)
        _uiState.update {
            it.copy(
                movieCardInfos = uiState.value.movieCardInfos.addAllDistinctly(
                    movies
                )
            )
        }
    }

    private fun defaultEmptyState() =
        MoviesUIState(
            movieCardInfos = persistentListOf(),
            selectedFilterMenu = FilterType.NowPlaying,
            menuItems = listOf(
                FilterMenuItem(
                    selected = true,
                    type = FilterType.NowPlaying
                ),
                FilterMenuItem(
                    selected = false,
                    type = FilterType.UpComing
                ),
                FilterMenuItem(
                    selected = false,
                    type = FilterType.TopRated
                ),
                FilterMenuItem(
                    selected = false,
                    type = FilterType.Popular
                )
            )
        )

    private fun ImmutableList<MovieCardInfo>.addAllDistinctly(
        newMovies: List<MovieCardInfo>
    ): ImmutableList<MovieCardInfo> {
        return toMutableList().apply {
            addAll(newMovies)
        }.distinctBy { it.movieId }
            .toPersistentList()
    }

    private fun List<FilterMenuItem>.updateAccordingToFilterType(newFilterType: FilterType): List<FilterMenuItem> {
        return map {
            it.copy(
                selected = it.type == newFilterType
            )
        }
    }
}
