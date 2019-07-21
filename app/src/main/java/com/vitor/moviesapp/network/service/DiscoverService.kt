package com.vitor.moviesapp.network.service

import com.vitor.moviesapp.model.SearchApiResponse
import com.vitor.moviesapp.util.NetworkConstants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverService {

    @GET(NetworkConstants.DISCOVER_ENDPOINT)
    fun getMovies(
        @Query(NetworkConstants.API_KEY_VALUE) apiKey: String,
        @Query(NetworkConstants.SORT_BY_VALUE) sortBy: String,
        @Query(NetworkConstants.PAGE_VALUE) page: Int,
        @Query(NetworkConstants.LANGUAGE_VALUE) language: String,
        @Query(NetworkConstants.ADULT_VALUE) adult: Boolean,
        @Query(NetworkConstants.MINIMUM_VOTES_VALUE) minimumVotes: Int
    ): Observable<SearchApiResponse>

}