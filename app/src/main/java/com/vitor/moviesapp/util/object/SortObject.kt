package com.vitor.moviesapp.util

object SortObject {

    const val SORT_BY_POPULARITY = "popularity.desc"
    const val SORT_BY_RELEASE_DATE = "release_date.desc"
    const val SORT_BY_AVERAGE = "vote_average.desc"
    const val SORT_BY_VOTE_COUNT = "vote_count.desc"

    var currentSortOrder = SORT_BY_POPULARITY

    enum class SortByEnum{
        POPULARITY,
        RELEASE_DATE,
        AVERAGE,
        VOTE_COUNT
    }

}