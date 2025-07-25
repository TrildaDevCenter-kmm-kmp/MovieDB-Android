package com.gabrielbmoro.moviedb.movies.di

import MoviesHandler
import com.gabrielbmoro.moviedb.domain.di.domainModule
import com.gabrielbmoro.moviedb.movies.ui.screens.movies.MoviesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.lazyModule
import org.koin.dsl.module

val featureMoviesModule = lazyModule {
    includes(domainModule)

    factory {
        MoviesHandler(
            repository = get(),
        )
    }
    viewModel {
        MoviesViewModel(
            ioDispatcher = Dispatchers.IO,
            loggerHelper = get(),
            moviesHandler = get(),
        )
    }
}
