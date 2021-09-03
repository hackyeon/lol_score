package com.hackyeon.lolscore

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackyeon.lolscore.adapter.DetailRecyclerViewAdapter
import com.hackyeon.lolscore.data.*
import com.hackyeon.lolscore.data.DataObject.championMap
import com.hackyeon.lolscore.data.DataObject.detail
import com.hackyeon.lolscore.data.DataObject.detailActivity
import com.hackyeon.lolscore.data.DataObject.imgRetrofitService
import com.hackyeon.lolscore.data.DataObject.retrofitService
import com.hackyeon.lolscore.data.DataObject.searchTier
import com.hackyeon.lolscore.data.DataObject.spellMap
import com.hackyeon.lolscore.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var championNameList = mutableListOf<String>()
    private var spell1NameList = mutableListOf<String>()
    private var spell2NameList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView(){
        detailActivity = this

        for(i in detail.participants){
            championNameList.add(championMap[i.championId]!!)
            spell1NameList.add(spellMap[i.spell1Id]!!)
            spell2NameList.add(spellMap[i.spell2Id]!!)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = DetailRecyclerViewAdapter(this, detail, championNameList, spell1NameList, spell2NameList)
    }

    fun searchSummoner(name: String){
        retrofitService.getSummoner(name)
            .enqueue(object : Callback<SummonerData> {
                override fun onResponse(call: Call<SummonerData>, response: Response<SummonerData>) {
                    if (response.isSuccessful) {
                        var body = response.body()!!
                        searchTier(body, this@DetailActivity, true)
                    }
                }
                override fun onFailure(call: Call<SummonerData>, t: Throwable) {
                }
            })
    }


}