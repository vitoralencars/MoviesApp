package com.vitor.moviesapp.model

import com.google.gson.annotations.SerializedName

data class GenreApiResponse (
    @SerializedName("genres")
    val genres: List<Genre>
)

data class Genre(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)