package com.vitor.moviesapp.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.vitor.moviesapp.model.Movie
import com.vitor.moviesapp.util.DataBaseConstants
import io.reactivex.Single

@Dao
interface FavoriteMovieDao {

    @Query(DataBaseConstants.SELECT_FAVORITES_MOVIES_QUERY)
    fun getFavoriteMovies(): Single<List<Movie>>

    @Query(DataBaseConstants.SELECT_FAVORITES_MOVIES_QUERY + " WHERE id = :movieId")
    fun getFavoriteMovieById(movieId: Long): Single<Movie>

    @Insert
    fun insertMovie(vararg movie: Movie)

    @Delete
    fun deleteMovie(vararg movie: Movie)

}