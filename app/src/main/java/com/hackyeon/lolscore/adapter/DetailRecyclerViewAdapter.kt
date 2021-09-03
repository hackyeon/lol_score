package com.hackyeon.lolscore.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hackyeon.lolscore.DetailActivity
import com.hackyeon.lolscore.MatchActivity
import com.hackyeon.lolscore.data.*
import com.hackyeon.lolscore.data.DataObject.glideImg
import com.hackyeon.lolscore.data.DataObject.matchActivity
import com.hackyeon.lolscore.data.database.Summoner
import com.hackyeon.lolscore.databinding.ItemDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class DetailRecyclerViewAdapter(private val context: Context, private val detail: Detail,
private val championNameList: MutableList<String>,
private val spell1NameList: MutableList<String>,
private val spell2NameList: MutableList<String>):
RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder>(){

    class ViewHolder(val binding: ItemDetailBinding): RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        glideImg(context, championNameList[position], holder.binding.championImageView, "champion")
        glideImg(context, spell1NameList[position], holder.binding.spellOneImageView, "spell")
        glideImg(context, spell2NameList[position], holder.binding.spellTwoImageView, "spell")
        holder.binding.levelTextView.text = detail.participants[position].stats.champLevel.toString()
        holder.binding.nameTextView.text = detail.participantIdentities[position].player.summonerName
        var kda = "${detail.participants[position].stats.kills} / ${detail.participants[position].stats.deaths} / ${detail.participants[position].stats.assists}"
        holder.binding.kdaTextView.text = kda
        glideImg(context, detail.participants[position].stats.item0, holder.binding.itemZeroImageView, "item")
        glideImg(context, detail.participants[position].stats.item1, holder.binding.itemOneImageView, "item")
        glideImg(context, detail.participants[position].stats.item2, holder.binding.itemTwoImageView, "item")
        glideImg(context, detail.participants[position].stats.item3, holder.binding.itemThreeImageView, "item")
        glideImg(context, detail.participants[position].stats.item4, holder.binding.itemFourImageView, "item")
        glideImg(context, detail.participants[position].stats.item5, holder.binding.itemFiveImageView, "item")
        glideImg(context, detail.participants[position].stats.item6, holder.binding.itemSixImageView, "item")

        var totalCs = detail.participants[position].stats.totalMinionsKilled + detail.participants[position].stats.neutralMinionsKilled
        holder.binding.minionKillTextView.text = totalCs.toString()
        var gold = detail.participants[position].stats.goldEarned / 1000.0
        holder.binding.goldTextView.text = "%.1fk".format(gold)
        var dec = DecimalFormat("#,###")
        holder.binding.damageTextView.text = dec.format(detail.participants[position].stats.totalDamageTaken)

        holder.binding.nameTextView.setOnClickListener {
            context as DetailActivity
            context.searchSummoner(detail.participantIdentities[position].player.summonerName)
        }
    }

    override fun getItemCount(): Int = detail.participants.size




}
