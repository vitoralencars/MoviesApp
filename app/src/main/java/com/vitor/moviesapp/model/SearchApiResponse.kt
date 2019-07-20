package com.vitor.moviesapp.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.vitor.moviesapp.util.DataBaseConstants
import com.vitor.moviesapp.util.LongConverter

data class SearchApiResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("results")
    val results: List<Movie>
)

@Entity(tableName = DataBaseConstants.MOVIE_TABLE_NAME)
@TypeConverters(LongConverter::class)
data class Movie(
    @PrimaryKey
    @SerializedName("id")
    val id: Long,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("genre_ids")
    val genreIds: List<Long>,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String
)
