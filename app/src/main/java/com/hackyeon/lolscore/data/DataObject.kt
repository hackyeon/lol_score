package com.hackyeon.lolscore.data

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.hackyeon.lolscore.DetailActivity
import com.hackyeon.lolscore.MainActivity
import com.hackyeon.lolscore.MatchActivity
import com.hackyeon.lolscore.R
//import com.hackyeon.lolscore.TestActivity
import com.hackyeon.lolscore.data.database.Summoner
import com.hackyeon.lolscore.data.database.SummonerDatabase
import com.hackyeon.lolscore.dialog_fragment.AlertDialogFragment
import com.hackyeon.lolscore.service.ImgRetrofitService
import com.hackyeon.lolscore.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

object DataObject {
    // 현재 고정키를 발급받지못해서 키를 수정하면서 사용중
    // 앱 완성 후에도 고정키 발급이 안될 경우 따로 키입력 만들고 상수 -> 변수
    const val API_KEY = "RGAPI-755543dc-c731-4833-9d67-54c39e7139c7"

    const val BASE_URL = "https://kr.api.riotgames.com/lol/"
    const val IMG_URL = "http://ddragon.leagueoflegends.com/"
    const val GET_SUMMONER = "summoner/v4/summoners/by-name/{name}"
    const val GET_TIER = "league/v4/entries/by-summoner/{encryptedSummonerId}"
    const val GET_MATCH_DATA = "match/v4/matchlists/by-account/{encryptedAccountId}"
    const val GET_MATCH_DETAIL = "match/v4/matches/{matchId}"
    const val GET_CHAMPION_JSON = "cdn/11.16.1/data/en_US/champion.json"
    const val GET_SPELL_JSON = "cdn/11.16.1/data/en_US/summoner.json"

    // 엑티비티
    var mainActivity: MainActivity? = null
    var matchActivity: MatchActivity? = null
    var detailActivity: DetailActivity? = null

    // 통신
    lateinit var retrofit: Retrofit
    lateinit var retrofitService: RetrofitService
    lateinit var imgRetrofit: Retrofit
    lateinit var imgRetrofitService: ImgRetrofitService

    // 데이터베이스
    lateinit var db: SummonerDatabase
    var summonerList = mutableListOf<Summoner>()
    var summonerNameList = mutableListOf<String>()

    // 챔피언, 스펠 맵
    var championMap = mutableMapOf<Int, String>()
    var spellMap = mutableMapOf<Int, String>()

    // 소환사 정보 데이터 db저장용
    var accountId: String = ""
    var name: String = ""
    var profileIconId = 0
    var summonerLevel: Int = 0
    var id: String = ""
    var tier: String = ""
    var rank: String = ""

    // 매치 상세정보
    lateinit var detail: Detail

    fun sendObjectData(summoner: Summoner) {
        accountId = summoner.accountId
        name = summoner.name
        profileIconId = summoner.profileIconId
        summonerLevel = summoner.summonerLevel
        id = summoner.id
        tier = summoner.tier
        rank = summoner.rank
    }

    fun createEmblemImg(tier: String, imgView: AppCompatImageView) {
        var imgSrc = when (tier) {
            "IRON" -> R.drawable.emblem_iron
            "BRONZE" -> R.drawable.emblem_bronze
            "SILVER" -> R.drawable.emblem_silver
            "GOLD" -> R.drawable.emblem_gold
            "PLATINUM" -> R.drawable.emblem_platinum
            "DIAMOND" -> R.drawable.emblem_diamond
            "MASTER" -> R.drawable.emblem_master
            "GRANDMASTER" -> R.drawable.emblem_grandmaster
            "CHALLENGER" -> R.drawable.emblem_challenger
            else -> R.drawable.no_img
        }

        if (imgSrc == R.drawable.no_img) imgView.visibility = GONE else imgView.visibility = VISIBLE

        imgView.setImageResource(imgSrc)
    }

    fun searchTier(body: SummonerData, context: Context, isDetailActivity: Boolean) {
        retrofitService.getTier(body.id).enqueue(object : Callback<Array<Tier>> {
            override fun onResponse(call: Call<Array<Tier>>, response: Response<Array<Tier>>) {
                if (response.isSuccessful) {
                    if(isDetailActivity) matchActivity?.finish()

                    var responseTier = "Unranked"
                    var responseRank = ""
                    if (!response.body().isNullOrEmpty()) {
                        for(i in response.body()!!){
                            if(i.queueType == "RANKED_SOLO_5x5"){
                                responseTier = i.tier
                                responseRank = i.rank
                            }
                        }
                    }

                    var summoner = Summoner(body.accountId, body.name, body.profileIconId, body.summonerLevel, body.id, responseTier, responseRank)

                    sendObjectData(summoner)
                    var intent = Intent(context, MatchActivity::class.java)
                    context.startActivity(intent)

                    Thread {
                        if (summonerNameList.contains(body.name)) {
                            db.summonerDao().deleteName(body.name)
                            var idx = summonerNameList.indexOf(body.name)
                            summonerNameList.removeAt(idx)
                            summonerList.removeAt(idx)
                        }

                        if (summonerList.size >= 10) {
                            var name = summonerNameList.first()
                            summonerList.removeFirst()
                            summonerNameList.removeFirst()
                            db.summonerDao().deleteName(name)
                        }

                        db.summonerDao().insert(summoner)
                        summonerList.add(summoner)
                        summonerNameList.add(summoner.name)

                        mainActivity?.updateRecyclerView()

                    }.start()
                    if(isDetailActivity) detailActivity?.finish()
                }
            }

            override fun onFailure(call: Call<Array<Tier>>, t: Throwable) {
            }
        })
    }

    fun glideImg(context: Context, nameOrNum: Any?, imageView: AppCompatImageView, type: String) {
        Glide.with(context)
            .load("http://ddragon.leagueoflegends.com/cdn/11.16.1/img/$type/${nameOrNum}.png")
            .placeholder(R.drawable.no_img)
            .centerCrop()
            .into(imageView)
    }
}