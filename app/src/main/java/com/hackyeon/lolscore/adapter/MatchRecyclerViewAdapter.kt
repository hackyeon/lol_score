package com.hackyeon.lolscore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hackyeon.lolscore.data.database.Summoner
import com.hackyeon.lolscore.databinding.ItemMatchBinding

class MatchRecyclerViewAdapter(private val dataSet: MutableList<Summoner>): RecyclerView.Adapter<MatchRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMatchBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = dataSet.size
}