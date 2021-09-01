package com.hackyeon.lolscore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hackyeon.lolscore.adapter.MatchRecyclerViewAdapter
import com.hackyeon.lolscore.data.*
import com.hackyeon.lolscore.data.DataObject.BASE_URL
import com.hackyeon.lolscore.data.DataObject.accountId
import com.hackyeon.lolscore.data.DataObject.matchActivity
import com.hackyeon.lolscore.data.DataObject.name
import com.hackyeon.lolscore.data.DataObject.profileIconId
import com.hackyeon.lolscore.data.DataObject.rank
import com.hackyeon.lolscore.data.DataObject.summonerLevel
import com.hackyeon.lolscore.data.DataObject.tier
import com.hackyeon.lolscore.databinding.ActivityMatchBinding
import com.hackyeon.lolscore.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMatchBinding
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitService: RetrofitService
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        loadData()
    }

    private fun initView() {
        matchActivity = this

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitService = retrofit.create(RetrofitService::class.java)

        Glide.with(this)
            .load("http://ddragon.leagueoflegends.com/cdn/11.16.1/img/profileicon/${profileIconId}.png")
            .centerInside()
            .into(binding.profileImageView)

        binding.apply {
            levelTextView.text = summonerLevel.toString()
            nameTextView.text = name
            tierTextView.text = if (rank == "") tier else "$tier $rank"
        }

        binding.matchRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.matchRecyclerView.adapter =
            MatchRecyclerViewAdapter(championImgList, detailMap, participantsMap, statsMap, spell1Map, spell2Map, matchList, this)
    }


    private fun loadData() {
        retrofitService.getMatch(accountId, endIndex, beginIndex)
            .enqueue(object : Callback<Matches> {
                override fun onResponse(call: Call<Matches>, response: Response<Matches>) {
                    if (response.isSuccessful) {
                        var matches = response.body()?.matches
                        if (matches?.size != 0) {
                            for (i in matches!!) {
                                matchList.add(i)
                                loadDataDetail(i, mapKey)
                                mapKey++
                            }
                            loadChampionImg(matches)
                        }
                    }
                }

                override fun onFailure(call: Call<Matches>, t: Throwable) {
                }
            })
    }

    private fun loadChampionImg(matches: MutableList<Match>) {
        var championImgRetrofit = Retrofit.Builder().baseUrl("http://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var championImgRetrofitService = championImgRetrofit.create(RetrofitService::class.java)

        championImgRetrofitService.getChampionImg().enqueue(object : Callback<ImgDataJson> {
            override fun onResponse(call: Call<ImgDataJson>, response: Response<ImgDataJson>) {
                if (response.isSuccessful) {

                    var data = response.body()?.data
                    var dataList = data?.keySet()

                    for (i in matches) {
                        for (j in dataList!!) {
                            if (i.champion.toString() == data?.getAsJsonObject(j)?.getAsJsonPrimitive("key")?.asString) {
                                championImgList.add(j)
                            }
                        }
                    }
                    binding.matchRecyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ImgDataJson>, t: Throwable) {
            }
        })

    }


    private fun loadDataDetail(match: Match, key: Int) {
        retrofitService.getDetail(match.gameId).enqueue(object : Callback<Detail> {
            override fun onResponse(call: Call<Detail>, response: Response<Detail>) {
                if (response.isSuccessful) {
                    var participants = response.body()?.participants
                    detailMap[key] = response.body()!!
                    for (i in participants!!) {
                        if (match.champion == i.championId) {
                            participantsMap[key] = i
                            statsMap[key] = i.stats
                        }
                    }
                    loadSpellImg(key, participantsMap[key]!!)
                }

            }

            override fun onFailure(call: Call<Detail>, t: Throwable) {
            }
        })
    }

    private fun loadSpellImg(key: Int, participants: Participants) {
        var spellImgRetrofit = Retrofit.Builder().baseUrl("http://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var spellImgRetrofitService = spellImgRetrofit.create(RetrofitService::class.java)

        spellImgRetrofitService.getSpellImg().enqueue(object : Callback<ImgDataJson> {
            override fun onResponse(call: Call<ImgDataJson>, response: Response<ImgDataJson>) {
                if (response.isSuccessful) {
                    var data = response.body()?.data
                    var dataList = data?.keySet()

                    for (i in dataList!!) {
                        if (participants.spell1Id.toString() == data?.getAsJsonObject(i)?.getAsJsonPrimitive("key")?.asString) {
                            spell1Map[key] = i
                        } else if (participants.spell2Id.toString() == data?.getAsJsonObject(i)?.getAsJsonPrimitive("key")?.asString) {
                            spell2Map[key] = i
                        }
                    }
                    binding.matchRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<ImgDataJson>, t: Throwable) {
            }
        })


    }


}