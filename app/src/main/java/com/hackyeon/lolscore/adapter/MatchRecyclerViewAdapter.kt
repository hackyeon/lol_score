package com.hackyeon.lolscore.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackyeon.lolscore.DetailActivity
import com.hackyeon.lolscore.R
import com.hackyeon.lolscore.data.DataObject.detail
import com.hackyeon.lolscore.data.DataObject.glideImg
import com.hackyeon.lolscore.data.Detail
import com.hackyeon.lolscore.data.Match
import com.hackyeon.lolscore.data.Participants
import com.hackyeon.lolscore.data.Stats
import com.hackyeon.lolscore.databinding.ItemMatchBinding
import java.text.SimpleDateFormat
import java.util.*

class MatchRecyclerViewAdapter(
    private val championImgList: MutableList<String>,
    private val detailMap: MutableMap<Int, Detail>,
    private val participantsMap: MutableMap<Int, Participants>,
    private val statsMap: MutableMap<Int, Stats>,
    private val spell1: MutableMap<Int, String>,
    private val spell2: MutableMap<Int, String>,
    private val matchList: MutableList<Match>,
    private val context: Context
) : RecyclerView.Adapter<MatchRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMatchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        glideImg(context, championImgList[position], holder.binding.championImageView, "champion")

        var resultColor =
            if (statsMap[position]?.win == true) R.color.win_blue else R.color.lose_red
        var resultText = if (statsMap[position]?.win == true) "승" else "패"
        if (detailMap[position]?.gameDuration != null) {
            var playHour = if (detailMap[position]?.gameDuration?.div(60)!! < 10) "0${
                (detailMap[position]?.gameDuration?.div(60))
            }" else "${(detailMap[position]?.gameDuration?.div(60))}"
            var playMinute = if (detailMap[position]?.gameDuration?.div(60)!! < 10) "0${
                detailMap[position]?.gameDuration?.div(60)
            }" else "${detailMap[position]?.gameDuration?.div(60)}"

            holder.binding.resultTextView.apply {
                setBackgroundColor(ContextCompat.getColor(context, resultColor))
                text = resultText
            }
            holder.binding.timeTextView.apply {
                setBackgroundColor(ContextCompat.getColor(context, resultColor))
                text = "$playHour:$playMinute"
            }
        }

        glideImg(context, statsMap[position]?.item0, holder.binding.itemZeroImageView, "item")
        glideImg(context, statsMap[position]?.item1, holder.binding.itemOneImageView, "item")
        glideImg(context, statsMap[position]?.item2, holder.binding.itemTwoImageView, "item")
        glideImg(context, statsMap[position]?.item3, holder.binding.itemThreeImageView, "item")
        glideImg(context, statsMap[position]?.item4, holder.binding.itemFourImageView, "item")
        glideImg(context, statsMap[position]?.item5, holder.binding.itemFiveImageView, "item")
        glideImg(context, statsMap[position]?.item6, holder.binding.itemSixImageView, "item")

        var kda =
            "${statsMap[position]?.kills} / ${statsMap[position]?.deaths} / ${statsMap[position]?.assists}"
        holder.binding.kdaTextView.text = kda

        glideImg(context, spell1[position], holder.binding.spellOneImageView, "spell")
        glideImg(context, spell2[position], holder.binding.spellTwoImageView, "spell")

        val sdf = SimpleDateFormat("MM/dd\nHH:mm")
        var gameDate = matchList[position].timestamp
        var test = Date(gameDate)
        var date = sdf.format(test)
        holder.binding.dateTextView.text = date

        holder.binding.matchLayout.setOnClickListener {
            if (detailMap[position] != null) {
                detail = detailMap[position]!!
                var intent = Intent(context, DetailActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = championImgList.size
}