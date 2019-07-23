package com.vitor.moviesapp.network.service

import com.vitor.moviesapp.model.SearchApiResponse
import com.vitor.moviesapp.util.constant.NetworkConstants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET(NetworkConstants.SEARCH_ENDPOINT)
    fun searchByQuery(
        @Query(NetworkConstants.API_KEY_VALUE) apiKey: String,
        @Query(NetworkConstants.LANGUAGE_VALUE) language: String,
        @Query(NetworkConstants.QUERY_VALUE) query: String,
        @Query(NetworkConstants.PAGE_VALUE) page: Int,
        @Query(NetworkConstants.ADULT_VALUE) adult: Boolean
    ): Observable<SearchApiResponse>

}