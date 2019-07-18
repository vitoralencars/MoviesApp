package com.vitor.moviesapp.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vitor.moviesapp.R
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.ui.MoviesActivity
import com.vitor.moviesapp.util.NetworkConstants
import com.vitor.moviesapp.util.loadImage
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviesAdapter(private val context: MoviesActivity)
    : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private var movies: List<Movie> = emptyList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies[position])

    fun updateList(movies: List<Movie>){
        this.movies = movies
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        fun bind(movie: Movie){
            with(itemView){
                tv_title_list.text = movie.title
                iv_poster_list.loadImage(NetworkConstants.BASE_POSTER_URL + movie.posterPath)
                rating_bar_list.rating = movie.voteAverage/2
                tv_vote_count_list.text = context.getString(
                    R.string.movie_item_count_indicator,
                    movie.voteCount.toString()
                )
                tv_adult_warning.visibility = if(movie.adult) View.VISIBLE else View.INVISIBLE
            }

            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            context.onItemClick(adapterPosition)
        }

    }
}