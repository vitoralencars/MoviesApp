package com.vitor.moviesapp.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vitor.moviesapp.util.NetworkConstants
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    val networkModule = module {
        single { gson }
        single { client }
        single { retrofit }
        single { discoverService }
        single { genreService }
    }

    private val gson: Gson = GsonBuilder().create()

    private val client: OkHttpClient = OkHttpClient.Builder().build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client)
        .build()

    private val discoverService: DiscoverService = retrofit.create(DiscoverService::class.java)
    private val genreService: GenreService = retrofit.create(GenreService::class.java)
}