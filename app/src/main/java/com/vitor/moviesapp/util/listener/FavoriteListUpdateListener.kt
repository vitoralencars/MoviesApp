package com.vitor.moviesapp.util.listener

import android.support.v7.widget.AppCompatImageView
import com.vitor.moviesapp.model.Movie

interface FavoriteListUpdateListener {

    fun onFavoriteIconClickedListener(movie: Movie, favoriteIcon: AppCompatImageView)

}