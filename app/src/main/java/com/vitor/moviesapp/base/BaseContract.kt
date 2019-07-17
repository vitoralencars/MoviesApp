package com.vitor.moviesapp.base

open class BaseContract {

    interface BaseView{

    }

    interface BasePresenter<in T>{
        fun attachView(view: T)
    }

}