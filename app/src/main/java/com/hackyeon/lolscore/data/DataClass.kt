package com.hackyeon.lolscore.data

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

data class Test(
    val gameId: Long
)