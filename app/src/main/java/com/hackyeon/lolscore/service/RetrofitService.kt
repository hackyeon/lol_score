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

    @GET(GET_SUMMONER)
    fun getSummoner(
        @Path("name")name: String
    ): Call<SummonerData>

    @GET(GET_TIER)
    fun getTier(
        @Path("encryptedSummonerId")id: String
    ): Call<Array<Tier>>

    @GET(GET_MATCH_DATA)
    fun getMatch(
        @Path("encryptedAccountId")accountId: String,
        @Query("endIndex")endIndex: Int,
        @Query("beginIndex")beginIndex: Int
    ): Call<Matches>

    @GET(GET_MATCH_DETAIL)
    fun getDetail(
        @Path("matchId")gameId: Long
    ): Call<Detail>

}