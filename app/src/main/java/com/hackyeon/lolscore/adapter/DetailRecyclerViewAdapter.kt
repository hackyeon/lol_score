package com.hackyeon.lolscore.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hackyeon.lolscore.DetailActivity
import com.hackyeon.lolscore.MatchActivity
import com.hackyeon.lolscore.R
import com.hackyeon.lolscore.data.*
import com.hackyeon.lolscore.data.DataObject.glideImg
import com.hackyeon.lolscore.data.DataObject.matchActivity
import com.hackyeon.lolscore.data.DataObject.name
import com.hackyeon.lolscore.data.database.Summoner
import com.hackyeon.lolscore.databinding.ItemDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class DetailRecyclerViewAdapter(private val context: Context, private var participants: MutableList<Participants>, private var participantIdentities: MutableList<ParticipantIdentities>, private val championNameList: MutableList<String>,
private val spell1NameList: MutableList<String>, private val spell2NameList: MutableList<String>):
RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder>(){

    class ViewHolder(val binding: ItemDetailBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(name == participantIdentities[position].player.summonerName) holder.binding.recyclerViewItemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.my_champion))
        glideImg(context, championNameList[position], holder.binding.championImageView, "champion")
        glideImg(context, spell1NameList[position], holder.binding.spellOneImageView, "spell")
        glideImg(context, spell2NameList[position], holder.binding.spellTwoImageView, "spell")
        holder.binding.levelTextView.text = participants[position].stats.champLevel.toString()
        holder.binding.nameTextView.text = participantIdentities[position].player.summonerName
        var kda = "${participants[position].stats.kills} / ${participants[position].stats.deaths} / ${participants[position].stats.assists}"
        holder.binding.kdaTextView.text = kda
        glideImg(context, participants[position].stats.item0, holder.binding.itemZeroImageView, "item")
        glideImg(context, participants[position].stats.item1, holder.binding.itemOneImageView, "item")
        glideImg(context, participants[position].stats.item2, holder.binding.itemTwoImageView, "item")
        glideImg(context, participants[position].stats.item3, holder.binding.itemThreeImageView, "item")
        glideImg(context, participants[position].stats.item4, holder.binding.itemFourImageView, "item")
        glideImg(context, participants[position].stats.item5, holder.binding.itemFiveImageView, "item")
        glideImg(context, participants[position].stats.item6, holder.binding.itemSixImageView, "item")

        var totalCs = participants[position].stats.totalMinionsKilled + participants[position].stats.neutralMinionsKilled
        holder.binding.minionKillTextView.text = totalCs.toString()
        var gold = participants[position].stats.goldEarned / 1000.0
        holder.binding.goldTextView.text = "%.1fk".format(gold)
        var dec = DecimalFormat("#,###")
        holder.binding.damageTextView.text = dec.format(participants[position].stats.totalDamageTaken)

        holder.binding.recyclerViewItemLayout.setOnClickListener {
            context as DetailActivity
            context.searchSummoner(participantIdentities[position].player.summonerName)
        }
    }

    override fun getItemCount(): Int = participants.size




}
