package com.vitor.moviesapp.util

object PageObject {

    var currentPage = 1
    var maxPages: Int = 1

    fun setNextPage(){
        currentPage++
    }

    fun resetPage(){
        currentPage = 1
    }

    fun hasMorePages() = currentPage <= maxPages
}