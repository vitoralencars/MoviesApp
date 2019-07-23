package com.vitor.moviesapp.database

import com.vitor.moviesapp.model.Movie

object FavoriteMoviesObject {

    var movies = listOf<Movie>()

    fun updateMoviesList(movies: List<Movie>){
        this.movies = movies
    }

    fun insertMovie(movie: Movie){
        if(!movies.contains(movie)) (movies as ArrayList).add(movie)
    }

    fun removeMovie(movie: Movie){
        if(movies.contains(movie)) (movies as ArrayList).remove(movie)
    }

}