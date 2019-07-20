package com.vitor.moviesapp.util

object DataBaseConstants {

    const val DATABASE_NAME = "MoviesAppDB"

    const val MOVIE_TABLE_NAME = "Movie"

    const val SELECT_FAVORITES_MOVIES_QUERY = "SELECT * FROM $MOVIE_TABLE_NAME"
}