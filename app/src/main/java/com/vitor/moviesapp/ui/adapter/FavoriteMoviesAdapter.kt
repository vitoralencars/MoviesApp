package com.vitor.moviesapp.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vitor.moviesapp.R
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.ui.activity.favoritemovies.FavoriteMoviesActivity
import com.vitor.moviesapp.util.DateUtils
import com.vitor.moviesapp.util.NetworkConstants
import com.vitor.moviesapp.util.loadImage
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoriteMoviesAdapter(private val activity: FavoriteMoviesActivity)
    : RecyclerView.Adapter<FavoriteMoviesAdapter.ViewHolder>(){

    private var favoriteMovies: List<Movie> = emptyList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_favorite, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = favoriteMovies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(favoriteMovies[position])

    fun updateList(favoriteMovies: List<Movie>){
        this.favoriteMovies = favoriteMovies
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(movie: Movie) {
            with(itemView) {
                tv_title_favorite.text = movie.title
                tv_overview_favorite.text = movie.overview
                iv_poster_favorite.loadImage(NetworkConstants.BASE_POSTER_URL + movie.posterPath)
                tv_release_year_favorite.text = activity.resources.getString(
                    R.string.parentheses_style,
                    DateUtils.getYear(movie.releaseDate)
                )

                btn_remove_movie.setOnClickListener{activity.removeMovie(movie)}
                btn_show_details.setOnClickListener{activity.showMovieDetails(movie)}
            }
        }

    }
}