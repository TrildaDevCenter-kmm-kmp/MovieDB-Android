package com.gabrielbmoro.moviedb.movies.di

import com.gabrielbmoro.moviedb.movies.domain.usecases.GetNowPlayingMoviesUseCase
import com.gabrielbmoro.moviedb.movies.domain.usecases.GetNowPlayingMoviesUseCaseImpl
import com.gabrielbmoro.moviedb.movies.domain.usecases.GetPopularMoviesUseCase
import com.gabrielbmoro.moviedb.movies.domain.usecases.GetPopularMoviesUseCaseImpl
import com.gabrielbmoro.moviedb.movies.domain.usecases.GetTopRatedMoviesUseCase
import com.gabrielbmoro.moviedb.movies.domain.usecases.GetTopRatedMoviesUseCaseImpl
import com.gabrielbmoro.moviedb.movies.domain.usecases.GetUpcomingMoviesUseCase
import com.gabrielbmoro.moviedb.movies.domain.usecases.GetUpcomingMoviesUseCaseImpl
import com.gabrielbmoro.moviedb.repository.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object MoviesModule {

    @Provides
    @ViewModelScoped
    fun getNowPlayingMoviesUseCase(repository: MoviesRepository): GetNowPlayingMoviesUseCase {
        return GetNowPlayingMoviesUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun getPopularMoviesUseCase(repository: MoviesRepository): GetPopularMoviesUseCase {
        return GetPopularMoviesUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun getTopRatedMoviesUseCase(repository: MoviesRepository): GetTopRatedMoviesUseCase {
        return GetTopRatedMoviesUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun getUpcomingMoviesUseCase(repository: MoviesRepository): GetUpcomingMoviesUseCase {
        return GetUpcomingMoviesUseCaseImpl(repository)
    }
}
