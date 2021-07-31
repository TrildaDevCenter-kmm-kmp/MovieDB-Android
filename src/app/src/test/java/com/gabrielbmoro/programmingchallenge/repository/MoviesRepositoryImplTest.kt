package com.gabrielbmoro.programmingchallenge.repository

import com.gabrielbmoro.programmingchallenge.repository.entities.Movie
import com.gabrielbmoro.programmingchallenge.repository.retrofit.ApiRepository
import com.gabrielbmoro.programmingchallenge.repository.room.FavoriteMoviesDAO
import com.gabrielbmoro.programmingchallenge.usecases.mappers.toFavoriteMovie
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MoviesRepositoryImplTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val apiRepository: ApiRepository = mockk()
    private val favoriteMoviesDAO: FavoriteMoviesDAO = mockk()

    private val mockedMovie = Movie(
        imageUrl = "https://asodaksd.jpg",
        popularity = 5f,
        votesAverage = 10f,
        language = "pt-BR",
        title = "The King Kong",
        isFavorite = false,
        overview = "This movie is a great one",
        releaseDate = "12/02/2002",
    )

    private fun getRepository(): MoviesRepositoryImpl {
        return MoviesRepositoryImpl(
            api = apiRepository,
            favoriteMoviesDAO = favoriteMoviesDAO
        )
    }

    @Test
    fun `should be able to favorite a movie that is not favorite`() {
        // arrange
        val repositoryTest = getRepository()
        val favoriteMovie = mockedMovie.toFavoriteMovie()
        coEvery { repositoryTest.checkIsAFavoriteMovie(favoriteMovie) }.returns(false)
        coEvery { favoriteMoviesDAO.saveFavorite(favoriteMovie) }.answers { }

        // act
        testDispatcher.runBlockingTest {
            val result = repositoryTest.doAsFavorite(favoriteMovie)

            // assert
            Truth.assertThat(result).isTrue()
        }

        coVerify { favoriteMoviesDAO.saveFavorite(favoriteMovie) }
    }

    @Test
    fun `should not be able to favorite a movie that is already favorite`() {
        // arrange
        val repositoryTest = getRepository()
        val favoriteMovie = mockedMovie.toFavoriteMovie()
        coEvery { repositoryTest.checkIsAFavoriteMovie(favoriteMovie) }.returns(true)

        // act
        testDispatcher.runBlockingTest {
            val result = repositoryTest.doAsFavorite(favoriteMovie)

            // assert
            Truth.assertThat(result).isTrue()
        }

        // assert
        coVerify(exactly = 0) { favoriteMoviesDAO.saveFavorite(favoriteMovie) }
    }

    @Test
    fun `check if the movie is favorite when there is one`() {
        // arrange
        val repositoryTest = getRepository()
        val favoriteMovie = mockedMovie.toFavoriteMovie()
        coEvery { favoriteMoviesDAO.isThereAMovie(favoriteMovie.title) }.returns(
            listOf(
                favoriteMovie
            )
        )

        // act
        testDispatcher.runBlockingTest {
            val isFavorite = repositoryTest.checkIsAFavoriteMovie(favoriteMovie)

            Truth.assertThat(isFavorite).isTrue()
        }
    }

    @Test
    fun `check if the movie is favorite when there is no one`() {
        // arrange
        val repositoryTest = getRepository()
        val favoriteMovie = mockedMovie.toFavoriteMovie()
        coEvery { favoriteMoviesDAO.isThereAMovie(favoriteMovie.title) }.returns(emptyList())

        // act
        testDispatcher.runBlockingTest {
            val isFavorite = repositoryTest.checkIsAFavoriteMovie(favoriteMovie)

            Truth.assertThat(isFavorite).isFalse()
        }
    }
}