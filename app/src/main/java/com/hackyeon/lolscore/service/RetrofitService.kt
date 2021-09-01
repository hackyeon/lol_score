package com.hackyeon.lolscore.service

import com.hackyeon.lolscore.data.*
import com.hackyeon.lolscore.data.DataObject.API_KEY
import com.hackyeon.lolscore.data.DataObject.GET_MATCH_DATA
import com.hackyeon.lolscore.data.DataObject.GET_MATCH_DETAIL
import com.hackyeon.lolscore.data.DataObject.GET_SUMMONER
import com.hackyeon.lolscore.data.DataObject.GET_TIER
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    @Headers(
        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36",
        "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7",
        "Accept-Charset: application/x-www-form-urlencoded; charset=UTF-8",
        "Origin: https://developer.riotgames.com",
        "X-Riot-Token: $API_KEY"
    )
    @GET(GET_SUMMONER)
    fun getSummoner(
        @Path("name")name: String
    ): Call<SummonerData>

    @Headers(
        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36",
        "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7",
        "Accept-Charset: application/x-www-form-urlencoded; charset=UTF-8",
        "Origin: https://developer.riotgames.com",
        "X-Riot-Token: $API_KEY"
    )
    @GET(GET_TIER)
    fun getTier(
        @Path("encryptedSummonerId")id: String
    ): Call<Array<Tier>>


    @Headers(
        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36",
        "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7",
        "Accept-Charset: application/x-www-form-urlencoded; charset=UTF-8",
        "Origin: https://developer.riotgames.com",
        "X-Riot-Token: $API_KEY"
    )
    @GET(GET_MATCH_DATA)
    fun getMatch(
        @Path("encryptedAccountId")accountId: String,
        @Query("endIndex")endIndex: Int,
        @Query("beginIndex")beginIndex: Int
    ): Call<Matches>

    @Headers(
        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36",
        "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7",
        "Accept-Charset: application/x-www-form-urlencoded; charset=UTF-8",
        "Origin: https://developer.riotgames.com",
        "X-Riot-Token: $API_KEY"
    )
    @GET(GET_MATCH_DETAIL)
    fun getDetail(
        @Path("matchId")gameId: Long
    ): Call<Detail>


    // 챔피언 레트로핏
    @GET("cdn/11.16.1/data/en_US/champion.json")
    fun getChampionImg(): Call<ImgDataJson>

    // 스펠
    @GET("cdn/11.16.1/data/en_US/summoner.json")
    fun getSpellImg(): Call<ImgDataJson>
}