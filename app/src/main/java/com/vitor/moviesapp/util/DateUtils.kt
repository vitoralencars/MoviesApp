package com.vitor.moviesapp.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
class DateUtils {

    companion object {
        fun getYear(date: String): String{
            val currentDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val dateFormat = SimpleDateFormat("yyyy")
            val dateObject = currentDateFormat.parse(date)

            return dateFormat.format(dateObject)
        }
    }

}