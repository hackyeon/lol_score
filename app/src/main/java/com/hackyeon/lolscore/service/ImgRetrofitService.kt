package com.hackyeon.lolscore.service

import com.hackyeon.lolscore.data.DataObject.GET_CHAMPION_JSON
import com.hackyeon.lolscore.data.DataObject.GET_SPELL_JSON
import com.hackyeon.lolscore.data.ImgDataJson
import retrofit2.Call
import retrofit2.http.GET

interface ImgRetrofitService {

    // 챔피언 레트로핏
    @GET(GET_CHAMPION_JSON)
    fun getChampionImg(): Call<ImgDataJson>

    // 스펠
    @GET(GET_SPELL_JSON)
    fun getSpellImg(): Call<ImgDataJson>

}