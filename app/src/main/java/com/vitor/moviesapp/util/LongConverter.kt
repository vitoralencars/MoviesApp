package com.vitor.moviesapp.util

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LongConverter {

    @TypeConverter
    fun convertListToJson(list: List<Long>?): String = Gson().toJson(list)

    @TypeConverter
    fun convertJsonToList(item: String): List<Long>?{
        val type = object : TypeToken<List<Long>>(){}.type
        return Gson().fromJson(item, type)
    }
}