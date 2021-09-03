package com.hackyeon.lolscore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.hackyeon.lolscore.data.*
import com.hackyeon.lolscore.data.DataObject.detail
import com.hackyeon.lolscore.data.DataObject.detailActivity
import com.hackyeon.lolscore.data.DataObject.name
import com.hackyeon.lolscore.data.DataObject.retrofitService
import com.hackyeon.lolscore.data.DataObject.searchTier
import com.hackyeon.lolscore.databinding.ActivityDetailBinding
import com.hackyeon.lolscore.fragment.DetailFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView(){
        detailActivity = this

        var timeStamp = intent.getLongExtra("timeStamp", 0)
        var playerIdx = -1

        for(i in detail.participantIdentities.indices){
            if(detail.participantIdentities[i].player.summonerName == name){
                playerIdx = i
            }
        }

        if(detail.participants[playerIdx].stats.win){
            binding.resultMainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.win_blue))
            binding.mainResultTextView.text = "승리"
        }else {
            binding.resultMainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.lose_red))
            binding.mainResultTextView.text = "패배"
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var gameTimeStamp = timeStamp
        var gameDate = Date(gameTimeStamp)
        var resultDate = sdf.format(gameDate)
        var playMinute = if (detail.gameDuration / 60 < 10) "0${detail.gameDuration / 60}" else "${detail.gameDuration / 60}"
        var playSecond = if (detail.gameDuration % 60 < 10) "0${detail.gameDuration % 60}" else "${detail.gameDuration % 60}"

        binding.dateTimeTextView.text = "$resultDate | $playMinute:$playSecond"

        createFragment(playerIdx)
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

    private fun createFragment(playerIdx: Int){
        var teamMap = mutableMapOf<Int, Team>()
        var participantsList = arrayOf(mutableListOf<Participants>(), mutableListOf<Participants>())
        var participantIdentitiesList = arrayOf(mutableListOf<ParticipantIdentities>(), mutableListOf<ParticipantIdentities>())

        for(i in detail.teams){
            if(i.teamId == detail.participants[playerIdx].teamId) teamMap[0] = i else teamMap[1] = i
        }

        for(i in detail.participants.indices){
            if(detail.participants[i].teamId == detail.participants[playerIdx].teamId){
                participantsList[0].add(detail.participants[i])
                participantIdentitiesList[0].add(detail.participantIdentities[i])
            } else{
                participantsList[1].add(detail.participants[i])
                participantIdentitiesList[1].add(detail.participantIdentities[i])
            }
        }

        supportFragmentManager.beginTransaction().add(R.id.myResultFragment, DetailFragment(teamMap[0]!!, participantsList[0], participantIdentitiesList[0])).commit()
        supportFragmentManager.beginTransaction().add(R.id.enemyResultFragment, DetailFragment(teamMap[1]!!, participantsList[1], participantIdentitiesList[1])).commit()
    }

}