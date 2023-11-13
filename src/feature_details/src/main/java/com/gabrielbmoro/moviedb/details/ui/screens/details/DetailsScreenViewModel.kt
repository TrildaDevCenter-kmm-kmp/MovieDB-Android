package com.gabrielbmoro.moviedb.details.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielbmoro.moviedb.details.domain.usecases.FavoriteMovieUseCase
import com.gabrielbmoro.moviedb.details.domain.usecases.GetMovieDetailsUseCase
import com.gabrielbmoro.moviedb.details.domain.usecases.IsFavoriteMovieUseCase
import com.gabrielbmoro.moviedb.repository.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val favoriteMovieUseCase: FavoriteMovieUseCase,
    private val isFavoriteMovieUseCase: IsFavoriteMovieUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {

    private lateinit var movie: Movie
    private val _uiState = MutableStateFlow(DetailsUIState.empty())
    val uiState = _uiState.stateIn(viewModelScope, SharingStarted.Eagerly, _uiState.value)

    fun setup(movie: Movie) {
        this.movie = movie
        this._uiState.update {
            DetailsUIState(
                imageUrl = this.movie.backdropImageUrl,
                movieLanguage = this.movie.language,
                isFavorite = this.movie.isFavorite,
                movieOverview = this.movie.overview,
                moviePopularity = this.movie.popularity,
                movieTitle = this.movie.title,
                movieVotesAverage = this.movie.votesAverage
            )
        }

        checkIfMovieIsFavorite(movie.title)

        fetchMoviesDetails()
    }

    private fun checkIfMovieIsFavorite(movieTitle: String) {
        viewModelScope.launch {
            val data = isFavoriteMovieUseCase.invoke(movieTitle)
            if (data.data != null) {
                _uiState.update {
                    it.copy(
                        isFavorite = data.data ?: false
                    )
                }
            }
        }
    }

    private fun fetchMoviesDetails() {
        viewModelScope.launch {
            updateLoadingState(true)

            getMovieDetailsUseCase(movieId = movie.id)
                .catch {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Something went wrong"
                        )
                    }
                }
                .collect { movieDetails ->
                    _uiState.update {
                        it.copy(
                            videoId = movieDetails.videoId,
                            tagLine = movieDetails.tagline,
                            status = movieDetails.status,
                            genres = movieDetails.genres,
                            homepage = movieDetails.homepage,
                            productionCompanies = movieDetails.productionCompanies.reduceToText()
                        )
                    }
                }
        }.invokeOnCompletion {
            updateLoadingState(false)
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun isToFavoriteOrUnFavorite(isToFavorite: Boolean) {
        viewModelScope.launch {
            val response = favoriteMovieUseCase(movie, isToFavorite)

            if (response.data != null) {
                movie.isFavorite = isToFavorite
                _uiState.update {
                    it.copy(
                        isFavorite = movie.isFavorite
                    )
                }
            }
        }
    }

    fun hideVideo() {
        _uiState.update {
            it.copy(showVideo = false)
        }
    }

    private fun List<String>.reduceToText() = reduce { acc, s -> "$acc, $s" }
}