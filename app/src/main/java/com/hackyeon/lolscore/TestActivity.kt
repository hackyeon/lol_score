package com.hackyeon.lolscore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hackyeon.lolscore.data.Tier
import com.hackyeon.lolscore.data.DataObject.BASE_URL
import com.hackyeon.lolscore.data.DataObject.matchActivity
import com.hackyeon.lolscore.data.DataObject.testActivity
import com.hackyeon.lolscore.data.TestImg
import com.hackyeon.lolscore.databinding.ActivityTestBinding
import com.hackyeon.lolscore.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var retrofit = Retrofit.Builder()
            .baseUrl("http://ddragon.leagueoflegends.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var retrofitService = retrofit.create(RetrofitService::class.java)


        retrofitService.getChampionImg().enqueue(object: Callback<TestImg>{

            override fun onResponse(call: Call<TestImg>, response: Response<TestImg>) {
                if(response.isSuccessful){


                    var test = response.body()?.data?.getAsJsonObject("Ahri")
                    Log.d("aabb", "$test")
                }

            }

            override fun onFailure(call: Call<TestImg>, t: Throwable) {
            }
        })

    }
}