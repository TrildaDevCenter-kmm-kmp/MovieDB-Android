package com.gabrielbmoro.moviedb.platform.navigation

import androidx.navigation.NavHostController


fun NavHostController.navigateToDetails(movieId: Long) {
    navigate(Screen.Details.route.plus("?$DETAILS_MOVIE_ID_ARGUMENT_KEY=$movieId"))
}

fun NavHostController.navigateToMovies() {
    navigate(Screen.Movies.route)
}

fun NavHostController.navigateToSearch(query: String) {
    navigate(Screen.Search.route.plus("?$SEARCH_QUERY_ARGUMENT_KEY=$query"))
}

fun NavHostController.navigateToWishlist() {
    navigate(Screen.Wishlist.route)
}