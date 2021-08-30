package com.hackyeon.lolscore.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackyeon.lolscore.MainActivity
import com.hackyeon.lolscore.MatchActivity
import com.hackyeon.lolscore.data.DataObject.sendObjectData
import com.hackyeon.lolscore.data.database.Summoner
import com.hackyeon.lolscore.databinding.ItemMainBinding

class MainRecyclerViewAdapter(private var dataSet: MutableList<Summoner>,private var context: Context): RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        var binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, tempPosition: Int) {
        var position = (dataSet.size-1) - tempPosition

        Glide.with(context)
            .load("http://ddragon.leagueoflegends.com/cdn/11.16.1/img/profileicon/${dataSet[position].profileIconId}.png")
            .centerInside()
            .into(holder.binding.profileImageView)

        holder.binding.levelTextView.text = dataSet[position].summonerLevel.toString()
        holder.binding.nameTextView.text = dataSet[position].name
        holder.binding.tierTextView.text = if(dataSet[position].rank == "") dataSet[position].tier else "${dataSet[position].tier} ${dataSet[position].rank}"

        clickedButton(holder, position)
    }

    override fun getItemCount(): Int = dataSet.size

    private fun clickedButton(holder: ViewHolder, position: Int){
        var main = context as MainActivity
        var idx = main.summonerNameList.indexOf(dataSet[position].name)
        var summoner = dataSet[position]
        var name = summoner.name

        holder.binding.itemLayout.setOnClickListener{
            sendObjectData(dataSet[position])
            context.startActivity(Intent(context, MatchActivity::class.java))

            Thread{
                main.apply {
                    db.summonerDao().deleteName(name)
                    summonerList.removeAt(idx)
                    summonerNameList.removeAt(idx)
                    db.summonerDao().insert(summoner)
                    summonerList.add(summoner)
                    summonerNameList.add(name)
                    runOnUiThread {
                        notifyDataSetChanged()
                    }
                }
            }.start()
        }

        holder.binding.deleteButton.setOnClickListener {
            Thread{
                main.apply {
                    db.summonerDao().deleteName(name)
                    summonerList.removeAt(idx)
                    summonerNameList.removeAt(idx)

                    runOnUiThread {
                        notifyDataSetChanged()
                        if (summonerList.size == 0){
                            recordVisibilityGone()
                        }
                    }
                }
            }.start()

        }
    }

}