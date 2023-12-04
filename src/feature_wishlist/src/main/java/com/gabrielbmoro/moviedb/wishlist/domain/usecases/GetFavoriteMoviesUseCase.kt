package com.gabrielbmoro.moviedb.wishlist.domain.usecases

import com.gabrielbmoro.moviedb.repository.MoviesRepository
import com.gabrielbmoro.moviedb.repository.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetFavoriteMoviesUseCase {
    operator fun invoke(): Flow<List<Movie>>
}

class GetFavoriteMoviesUseCaseImpl @Inject constructor(
    private val repository: MoviesRepository
) : GetFavoriteMoviesUseCase {

    override operator fun invoke(): Flow<List<Movie>> = repository.getFavoriteMovies()
}
