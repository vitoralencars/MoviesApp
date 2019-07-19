package com.vitor.moviesapp.base

import android.annotation.SuppressLint
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.view.View
import com.vitor.moviesapp.R
import com.vitor.moviesapp.database.AppDataBase
import com.vitor.moviesapp.model.Movie
import kotlinx.android.synthetic.main.progress_bar.*
import org.koin.android.ext.android.inject

@SuppressLint("Registered")
open class BaseActivity: AppCompatActivity(), BaseContract.BaseView {

    val database: AppDataBase by inject()

    override fun setLayoutReference(id: Int) {
        setContentView(id)
    }

    override fun showProgressBar() {
        progress_bar?.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress_bar?.visibility = View.GONE
    }

    override fun insertFavoriteMovie(movie: Movie) {
        database.favoriteMovieDao().insertMovie(movie)
    }

    override fun removeFavoriteMovie(movie: Movie) {
        database.favoriteMovieDao().deleteMovie(movie)
    }

    override fun checkFavoriteIcon(favoriteIcon: AppCompatImageView) {
        favoriteIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_checked))
    }

    override fun unCheckFavoriteIcon(favoriteIcon: AppCompatImageView) {
        favoriteIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_unchecked))
    }
}