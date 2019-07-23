package com.vitor.moviesapp.util.viewutil

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String){
    Glide.with(context)
        .load(url)
        .fitCenter()
        .into(this)
}