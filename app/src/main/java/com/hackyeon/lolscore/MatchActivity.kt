package com.hackyeon.lolscore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.hackyeon.lolscore.data.DataObject.BASE_URL
import com.hackyeon.lolscore.data.DataObject.accountId
import com.hackyeon.lolscore.data.DataObject.id
import com.hackyeon.lolscore.data.DataObject.matchActivity
import com.hackyeon.lolscore.data.DataObject.name
import com.hackyeon.lolscore.data.DataObject.profileIconId
import com.hackyeon.lolscore.data.DataObject.rank
import com.hackyeon.lolscore.data.DataObject.summonerLevel
import com.hackyeon.lolscore.data.DataObject.tier
import com.hackyeon.lolscore.data.Match
import com.hackyeon.lolscore.data.Matches
import com.hackyeon.lolscore.data.TestImg
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
    private var beginIndex =  0
    private var endIndex = 4
    private var matchList = mutableListOf<Match>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        loadData()
    }

    private fun initView(){
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
            tierTextView.text = if(rank == "") tier else "$tier $rank"
        }
    }






    private fun loadData(){
        retrofitService.getMatch(accountId, endIndex, beginIndex).enqueue(object: Callback<Matches> {
            override fun onResponse(call: Call<Matches>, response: Response<Matches>) {
                if(response.isSuccessful){
                    var matches = response.body()?.matches
                    if(matches?.size != 0){
                        for(i in matches!!){
                            matchList.add(i)
                        }




                    }


                }


            }

            override fun onFailure(call: Call<Matches>, t: Throwable) {
            }
        })
    }


    private fun loadDataDetail(){

    }


}