package com.hackyeon.lolscore.service

import com.hackyeon.lolscore.data.DataObject.GET_CHAMPION_JSON
import com.hackyeon.lolscore.data.DataObject.GET_SPELL_JSON
import com.hackyeon.lolscore.data.ImgDataJson
import retrofit2.Call
import retrofit2.http.GET

interface ImgRetrofitService {

    @GET(GET_CHAMPION_JSON)
    fun getChampionImg(): Call<ImgDataJson>

    @GET(GET_SPELL_JSON)
    fun getSpellImg(): Call<ImgDataJson>

}