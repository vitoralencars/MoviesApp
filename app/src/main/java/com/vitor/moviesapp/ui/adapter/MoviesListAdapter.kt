package com.vitor.moviesapp.ui.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vitor.moviesapp.R
import com.vitor.moviesapp.database.FavoriteMoviesObject
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.ui.activity.movieslist.MoviesListActivity
import com.vitor.moviesapp.util.NetworkConstants
import com.vitor.moviesapp.util.loadImage
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviesListAdapter(private val activity: MoviesListActivity)
    : RecyclerView.Adapter<MoviesListAdapter.ViewHolder>() {

    private var remoteMovies: List<Movie> = emptyList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_movie, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = remoteMovies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(remoteMovies[position])

    fun updateList(remoteMovies: List<Movie>){
        this.remoteMovies = remoteMovies
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        fun bind(movie: Movie){
            with(itemView){
                tv_title_list.text = movie.title
                iv_poster_list.loadImage(NetworkConstants.BASE_POSTER_URL + movie.posterPath)
                rating_bar_list.rating = movie.voteAverage/2
                tv_vote_count_list.text = context.getString(
                    R.string.parentheses_style,
                    movie.voteCount.toString()
                )
                tv_adult_warning_list.visibility = if(movie.adult) View.VISIBLE else View.INVISIBLE
                if(movie in FavoriteMoviesObject.movies)
                    iv_favorite_list.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_checked))
                else
                    iv_favorite_list.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_unchecked))

                iv_favorite_list.setOnClickListener{
                    activity.onFavoriteIconClickedListener(movie, iv_favorite_list)
                }
            }

            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            activity.onItemClick(adapterPosition)
        }

    }
}