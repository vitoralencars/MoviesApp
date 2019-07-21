package com.vitor.moviesapp.network.service

import com.vitor.moviesapp.model.GenreApiResponse
import com.vitor.moviesapp.util.NetworkConstants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GenreService {

    @GET(NetworkConstants.GENRE_ENDPOINT)
    fun getGenres(
        @Query(NetworkConstants.API_KEY_VALUE) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_VALUE) language: String
    ): Observable<GenreApiResponse>

}