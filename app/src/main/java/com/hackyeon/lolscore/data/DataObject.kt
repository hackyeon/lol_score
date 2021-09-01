package com.hackyeon.lolscore.data

import com.hackyeon.lolscore.MainActivity
import com.hackyeon.lolscore.MatchActivity
import com.hackyeon.lolscore.TestActivity
import com.hackyeon.lolscore.data.database.Summoner

object DataObject {
    const val API_KEY = "RGAPI-fae158ed-53c4-4c67-b6cb-9652ea40a961"

    const val BASE_URL = "https://kr.api.riotgames.com/lol/"
    const val GET_SUMMONER = "summoner/v4/summoners/by-name/{name}"
    const val GET_TIER = "league/v4/entries/by-summoner/{encryptedSummonerId}"
    const val GET_MATCH_DATA = "match/v4/matchlists/by-account/{encryptedAccountId}"
    const val GET_MATCH_DETAIL = "match/v4/matches/{matchId}"

    var mainActivity: MainActivity? =null
    var matchActivity: MatchActivity? = null

    // 테스트용
    var testActivity: TestActivity? = null

    var accountId: String = ""
    var name: String = ""
    var profileIconId = 0
    var summonerLevel: Int = 0
    var id: String = ""
    var tier: String = ""
    var rank: String = ""

    fun sendObjectData(summoner: Summoner) {
        accountId = summoner.accountId
        name = summoner.name
        profileIconId = summoner.profileIconId
        summonerLevel = summoner.summonerLevel
        id = summoner.id
        tier = summoner.tier
        rank = summoner.rank
    }

}