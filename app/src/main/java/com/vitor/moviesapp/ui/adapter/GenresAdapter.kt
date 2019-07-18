package com.vitor.moviesapp.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vitor.moviesapp.R
import com.vitor.moviesapp.model.Genre
import kotlinx.android.synthetic.main.item_genrer.view.*

class GenresAdapter(private val context: Context, private val genres: List<Genre>)
    : RecyclerView.Adapter<GenresAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_genrer, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = genres.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(genres[position])

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(genre: Genre){
            with(itemView){
                tv_genrer.text = genre.name
            }
        }
    }

}