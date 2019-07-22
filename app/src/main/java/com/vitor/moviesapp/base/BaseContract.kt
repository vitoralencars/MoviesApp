package com.vitor.moviesapp.base

open class BaseContract {

    interface BaseView{
        fun setLayoutReference(id: Int)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface BasePresenter<in T>{
        fun attachView(view: T)
    }

}