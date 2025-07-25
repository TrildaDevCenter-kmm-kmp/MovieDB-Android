package com.gabrielbmoro.moviedb.domain.mappers

import kotlin.test.Test
import kotlin.test.assertEquals

class MappersExtTest {
    @Test
    fun `should be able to convert a movie to a dto`() {
        // arrange
        val (parameter, expected) = movieAndFavoriteMovieDTO

        // act
        val result = parameter.toFavoriteMovieDTO()

        // assert
        assertEquals(expected, result)
    }

    @Test
    fun `should be able to convert dto to movie object`() {
        // arrange
        val (expected, parameter) = movieAndFavoriteMovieDTO

        // act
        val result = parameter.toMovie()

        // assert
        assertEquals(expected, result)
    }

    @Test
    fun `should be able to convert a response to movie object`() {
        // arrange
        val (expected, parameter) = movieAndMovieResponse

        // act
        val result = parameter.toMovie()

        // assert
        assertEquals(expected, result)
    }

    @Test
    fun `should be able to convert a response to details object`() {
        // arrange
        val (expected, parameter) = movieDetailsAndMovieDetailsResponse

        // act
        val result = parameter.toMovieDetail()

        // assert
        assertEquals(expected, result)
    }

    @Test
    fun `should be able to convert a video stream response to a video stream objects`() {
        // arrange
        val (expected, parameter) = videoStreamAndVideoStreamResponse

        // act
        val result = parameter.toVideoStreams()

        // assert
        assertEquals(expected, result)
        assertEquals(expected[0], result[0])
    }
}
