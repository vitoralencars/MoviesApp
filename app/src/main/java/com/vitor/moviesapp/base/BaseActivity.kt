package com.vitor.moviesapp.base

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.vitor.moviesapp.database.AppDataBase
import kotlinx.android.synthetic.main.progress_bar.*
import org.koin.android.ext.android.inject

@SuppressLint("Registered")
open class BaseActivity: AppCompatActivity(), BaseContract.BaseView {

    val database: AppDataBase by inject()

    override fun setLayoutReference(id: Int) = setContentView(id)

    override fun showProgressBar() {
        progress_bar?.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress_bar?.visibility = View.GONE
    }
}