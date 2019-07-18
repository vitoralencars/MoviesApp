package com.vitor.moviesapp.util

object SortUtils {

    const val SORT_BY_POPULARITY = "popularity.desc"
    const val SORT_BY_RELEASE_DATE = "release_date.desc"
    const val SORT_BY_AVERAGE = "vote_average.desc"
    const val SORT_BY_VOTE_COUNT = "vote_count.desc"

    enum class SortByEnum(index: Int) {
        POPULARITY(0),
        RELEASE_DATE(1),
        AVERAGE(2),
        VOTE_COUNT(3)
    }

}