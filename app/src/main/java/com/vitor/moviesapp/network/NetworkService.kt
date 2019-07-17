package com.vitor.moviesapp.network

import com.vitor.moviesapp.model.SearchApiResponse
import com.vitor.moviesapp.util.NetworkConstants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {

    @GET("movie")
    fun getMovies(
        @Query(NetworkConstants.API_KEY_VALUE) apiKey: String,
        @Query(NetworkConstants.PAGE_VALUE) page: Int,
        @Query(NetworkConstants.LANGUAGE_VALUE) language: String
    ): Observable<SearchApiResponse>

}