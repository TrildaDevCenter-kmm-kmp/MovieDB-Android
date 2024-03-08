package com.gabrielbmoro.moviedb.domain.usecases

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class SearchMoviesUseCaseImplTest {
    private lateinit var repository: FakeRepository
    private lateinit var useCase: SearchMovieUseCase

    @Before
    fun before() {
        repository = FakeRepository()
        useCase = SearchMovieUseCaseImpl(repository)
    }

    @Test
    fun `should be able to search for movies`() = runTest {
        // arrange
        repository.searchMovies = emptyList()

        // act
        val result = useCase.execute(SearchMovieUseCase.Params("query"))

        // assert
        assertEquals(emptyList(), result)
    }
}