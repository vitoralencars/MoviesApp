package com.vitor.moviesapp.util

import android.annotation.SuppressLint
import android.widget.TextView
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun TextView.formatDate_ddMMyyyy(date: String){
    val format = SimpleDateFormat("dd/MM/yyyy")
    val date = format.parse(date)
    this.text = format.format(date)
}