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
    val queueType: String,
    val tier: String,
    val rank: String
)

data class Matches(
    val matches: MutableList<Match>,
)

data class Match(
    val gameId: Long,
    val champion: Int,
    val lane: String,
    val timestamp: Long
)

// 이게 매치 상세데이터 가져올 준비중인 클래스 현재 테스트중
data class Detail(
    val gameDuration: Long,
    val teams: MutableList<Team>,
    val participants: MutableList<Participants>,
    val participantIdentities: MutableList<ParticipantIdentities>
)

data class ParticipantIdentities(
    val player: Player
)
data class Player(
    val summonerName: String
)

data class Team(
    val win: String,
    val teamId: Int,
    val towerKills: Int,
    val baronKills: Int,
    val dragonKills: Int
)

data class Participants(
    val championId: Int,
    val spell1Id: Int,
    val spell2Id: Int,
    val stats: Stats
)
data class Stats(
    val win: Boolean,
    val item0: Int,
    val item1: Int,
    val item2: Int,
    val item3: Int,
    val item4: Int,
    val item5: Int,
    val item6: Int,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val goldEarned: Int,
    val totalDamageTaken: Long,
    val champLevel: Int,
    val totalMinionsKilled: Int,
    val neutralMinionsKilled: Int
)

// 챔피언,스펠 이미지
data class ImgDataJson(
    val data: JsonObject
)

