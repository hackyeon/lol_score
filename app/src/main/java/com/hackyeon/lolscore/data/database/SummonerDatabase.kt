package com.hackyeon.lolscore.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Summoner::class], version = 1)
abstract class SummonerDatabase : RoomDatabase() {
    abstract fun summonerDao(): SummonerDao

    companion object {
        private var instance: SummonerDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SummonerDatabase? {
            if (instance == null) {
                synchronized(SummonerDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SummonerDatabase::class.java,
                        "summoner-database"
                    ).build()
                }
            }
            return instance
        }
    }

}