package com.hackyeon.lolscore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hackyeon.lolscore.data.Tier
import com.hackyeon.lolscore.data.DataObject.BASE_URL
import com.hackyeon.lolscore.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        Log.d("aabb", "!23")

        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var retrofitService = retrofit.create(RetrofitService::class.java)


        retrofitService.getTier("1NbO21pd4Sa6xMhD_oLPjrbfSiSQbjX01sxjWEzKh2w8-IA").enqueue(object: Callback<Array<Tier>>{

            override fun onResponse(call: Call<Array<Tier>>, response: Response<Array<Tier>>) {
                Log.d("aabb", "통신 시작")
                if(response.isSuccessful){
                    Log.d("aabb", "통신 성공")
                    var body = response.body()

                    Log.d("aabb", "${body?.get(0)?.tier}")
                    Log.d("aabb", "${body?.get(0)?.rank}")

                }else{
                    Log.d("aabb", "통신 실패")
                }


            }

            override fun onFailure(call: Call<Array<Tier>>, t: Throwable) {
                Log.d("aabb", "${call} - ${t}")
                Log.d("aabb", "인터넷 불량")
            }
        })


    }
}