package com.hackyeon.lolscore.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Summoner(
    val accountId: String,
    val name: String,
    val profileIconId: Int,
    val summonerLevel: Int,
    val id: String,
    val tier: String,
    val rank: String
){
    @PrimaryKey(autoGenerate = true) var idx: Int = 0
}