package com.hackyeon.lolscore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackyeon.lolscore.data.database.Summoner
import com.hackyeon.lolscore.databinding.ItemMatchBinding

class MatchRecyclerViewAdapter(private val dataSet: MutableList<Summoner>, private val context: Context): RecyclerView.Adapter<MatchRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMatchBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Glide.with(context)
//            .load("http://ddragon.leagueoflegends.com/cdn/11.16.1/img/champion/${temp}.png")
//            .centerInside()
//            .into(holder.binding.testImageView)

    }

    override fun getItemCount(): Int = dataSet.size
}