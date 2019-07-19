package com.vitor.moviesapp.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.vitor.moviesapp.database.dao.FavoriteMovieDao
import com.vitor.moviesapp.model.Movie

@Database(version = 1, entities = [Movie::class], exportSchema = false)
abstract class AppDataBase : RoomDatabase(){

    abstract fun favoriteMovieDao(): FavoriteMovieDao

}