package com.vitor.moviesapp.network

import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.util.NetworkConstants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkService {

    @GET("movie")
    fun getMovies(
        @Path(NetworkConstants.API_KEY_VALUE) apiKey: String,
        @Path(NetworkConstants.PAGE_VALUE) page: Int
    ): Observable<Movie>

}