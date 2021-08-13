package com.gabrielbmoro.programmingchallenge.core.di

import android.content.Context
import com.gabrielbmoro.programmingchallenge.R
import com.gabrielbmoro.programmingchallenge.repository.MoviesRepository
import com.gabrielbmoro.programmingchallenge.repository.MoviesRepositoryImpl
import com.gabrielbmoro.programmingchallenge.repository.retrofit.ApiRepository
import com.gabrielbmoro.programmingchallenge.repository.room.FavoriteMoviesDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideRepositoryInstance(
        apiRepository: ApiRepository,
        favoriteMoviesDAO: FavoriteMoviesDAO,
        @ApplicationContext context: Context
    ): MoviesRepository {
        return MoviesRepositoryImpl(
            api = apiRepository,
            favoriteMoviesDAO = favoriteMoviesDAO,
            apiToken = context.getString(R.string.api_token)
        )
    }
}