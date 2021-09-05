package com.hackyeon.lolscore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hackyeon.lolscore.adapter.MatchRecyclerViewAdapter
import com.hackyeon.lolscore.data.*
import com.hackyeon.lolscore.data.DataObject.BASE_URL
import com.hackyeon.lolscore.data.DataObject.IMG_URL
import com.hackyeon.lolscore.data.DataObject.accountId
import com.hackyeon.lolscore.data.DataObject.championMap
import com.hackyeon.lolscore.data.DataObject.createEmblemImg
import com.hackyeon.lolscore.data.DataObject.imgRetrofit
import com.hackyeon.lolscore.data.DataObject.imgRetrofitService
import com.hackyeon.lolscore.data.DataObject.matchActivity
import com.hackyeon.lolscore.data.DataObject.name
import com.hackyeon.lolscore.data.DataObject.profileIconId
import com.hackyeon.lolscore.data.DataObject.rank
import com.hackyeon.lolscore.data.DataObject.retrofitService
import com.hackyeon.lolscore.data.DataObject.spellMap
import com.hackyeon.lolscore.data.DataObject.summonerLevel
import com.hackyeon.lolscore.data.DataObject.tier
import com.hackyeon.lolscore.databinding.ActivityMatchBinding
import com.hackyeon.lolscore.service.ImgRetrofitService
import com.hackyeon.lolscore.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMatchBinding
    private var beginIndex = 0
    private var endIndex = 5
    private var matchList = mutableListOf<Match>()
    private var championImgList = mutableListOf<String>()
    private var detailMap = mutableMapOf<Int, Detail>()
    private var participantsMap = mutableMapOf<Int, Participants>()
    private var statsMap = mutableMapOf<Int, Stats>()
    private var spell1Map = mutableMapOf<Int, String>()
    private var spell2Map = mutableMapOf<Int, String>()
    private var mapKey = 0
    private var isLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        loadData()
        createListener()
    }

    private fun createListener() {
        binding.matchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!binding.matchRecyclerView.canScrollVertically(1)) {
                    if (!isLoading) {
                        isLoading = true
                        binding.loadingProgressBar.visibility = VISIBLE
                        beginIndex += 5
                        endIndex += 5
                        loadData()
                    }
                }
            }
        })

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initView() {
        matchActivity = this

        Glide.with(this)
            .load("http://ddragon.leagueoflegends.com/cdn/11.16.1/img/profileicon/${profileIconId}.png")
            .placeholder(R.drawable.no_img)
            .centerCrop()
            .into(binding.profileImageView)

        binding.apply {
            levelTextView.text = summonerLevel.toString()
            nameTextView.text = name
            tierTextView.text = if (rank == "") tier else "$tier $rank"
        }

        createEmblemImg(tier, binding.emblemImageView)

        binding.matchRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.matchRecyclerView.adapter =
            MatchRecyclerViewAdapter(
                championImgList,
                detailMap,
                participantsMap,
                statsMap,
                spell1Map,
                spell2Map,
                matchList,
                this
            )
    }

    private fun loadData() {
        retrofitService.getMatch(accountId, endIndex, beginIndex)
            .enqueue(object : Callback<Matches> {
                override fun onResponse(call: Call<Matches>, response: Response<Matches>) {
                    if (response.isSuccessful) {
                        var matches = response.body()?.matches
                        if (!matches.isNullOrEmpty()) {

                            var dataLoadSuccess = mutableListOf<Boolean>()
                            for(i in matches){
                                dataLoadSuccess.add(false)
                            }

                            for (i in matches.indices) {
                                matchList.add(matches[i])
                                championImgList.add(championMap[matches[i].champion]!!)
//                                binding.matchRecyclerView.adapter?.notifyItemInserted(championImgList.size - 1)
                                loadDataDetail(matches[i], mapKey, dataLoadSuccess, i)
                                mapKey++
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<Matches>, t: Throwable) {
                }
            })
    }

    private fun loadDataDetail(match: Match, key: Int, dataLoadSuccess: MutableList<Boolean>, count: Int) {
        retrofitService.getDetail(match.gameId).enqueue(object : Callback<Detail> {
            override fun onResponse(call: Call<Detail>, response: Response<Detail>) {
                if (response.isSuccessful) {
                    var participants = response.body()?.participants
                    detailMap[key] = response.body()!!
                    for (i in participants!!) {
                        if (match.champion == i.championId) {
                            participantsMap[key] = i
                            statsMap[key] = i.stats
                            spell1Map[key] = spellMap[i.spell1Id]!!
                            spell2Map[key] = spellMap[i.spell2Id]!!
                        }
                    }

                    dataLoadSuccess[count] = true
                    var loadingCnt = 0
                    for(i in dataLoadSuccess){
                        if(i) loadingCnt++
                    }
                    if(loadingCnt == dataLoadSuccess.size){
                        binding.loadingProgressBar.visibility = GONE
                        binding.matchRecyclerView.adapter?.notifyDataSetChanged()
                        isLoading = false
                    }
                }
            }

            override fun onFailure(call: Call<Detail>, t: Throwable) {
            }
        })
    }

}