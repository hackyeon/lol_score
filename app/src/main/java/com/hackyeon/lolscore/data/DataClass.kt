package com.hackyeon.lolscore.data

import com.google.gson.JsonObject

data class SummonerData(
    val id: String,
    val accountId: String,
    val name: String,
    val profileIconId: Int,
    val summonerLevel: Int
)

data class Tier(
    val tier: String,
    val rank: String
)

data class Matches(
    val matches: MutableList<Match>
)

data class Match(
    val gameId: Long,
    val champion: Int,
    val lane: String,
    val timestamp: Long
)

// 이게 매치 상세데이터 가져올 준비중인 클래스 현재 테스트중
data class Test(
    val gameId: Long
)
// 테스트용
data class TestImg(
    val data: JsonObject
)
