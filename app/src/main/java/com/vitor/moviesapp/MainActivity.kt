package com.vitor.moviesapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.vitor.moviesapp.network.NetworkService
import com.vitor.moviesapp.util.NetworkConstants
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    val networkService: NetworkService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkService.getMovies(NetworkConstants.API_KEY, 1)
    }
}
