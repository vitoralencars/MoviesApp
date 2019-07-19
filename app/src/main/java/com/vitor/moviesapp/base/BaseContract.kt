package com.vitor.moviesapp.base

import android.support.v7.widget.AppCompatImageView
import com.vitor.moviesapp.model.Movie

open class BaseContract {

    interface BaseView{
        fun setLayoutReference(id: Int)
        fun showProgressBar()
        fun hideProgressBar()
        fun insertFavoriteMovie(movie: Movie)
        fun removeFavoriteMovie(movie: Movie)
        fun checkFavoriteIcon(favoriteIcon: AppCompatImageView)
        fun unCheckFavoriteIcon(favoriteIcon: AppCompatImageView)
    }

    interface BasePresenter<in T>{
        fun attachView(view: T)
    }

}