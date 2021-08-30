package com.hackyeon.lolscore.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SummonerDao {
    @Insert
    fun insert(summoner: Summoner)

    @Query("DELETE FROM Summoner WHERE idx = :idx")
    fun deleteIdx(idx: Int)

    @Query("DELETE FROM Summoner WHERE name = :name")
    fun deleteName(name: String)

    @Query("SELECT * FROM Summoner")
    fun getAll(): MutableList<Summoner>

}