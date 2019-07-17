package com.vitor.moviesapp.model

import com.google.gson.annotations.SerializedName

data class Movie(
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
    val genreIds: List<Int>,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String
)
