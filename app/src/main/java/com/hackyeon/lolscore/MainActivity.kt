package com.hackyeon.lolscore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import com.hackyeon.lolscore.adapter.MainRecyclerViewAdapter
import com.hackyeon.lolscore.data.DataObject
import com.hackyeon.lolscore.data.SummonerData
import com.hackyeon.lolscore.data.DataObject.BASE_URL
import com.hackyeon.lolscore.data.DataObject.championMap
import com.hackyeon.lolscore.data.DataObject.db
import com.hackyeon.lolscore.data.DataObject.imgRetrofit
import com.hackyeon.lolscore.data.DataObject.imgRetrofitService
import com.hackyeon.lolscore.data.DataObject.mainActivity
import com.hackyeon.lolscore.data.DataObject.retrofit
import com.hackyeon.lolscore.data.DataObject.retrofitService
import com.hackyeon.lolscore.data.DataObject.searchTier
import com.hackyeon.lolscore.data.DataObject.spellMap
import com.hackyeon.lolscore.data.DataObject.summonerList
import com.hackyeon.lolscore.data.DataObject.summonerNameList
import com.hackyeon.lolscore.data.ImgDataJson
import com.hackyeon.lolscore.data.database.SummonerDatabase
import com.hackyeon.lolscore.databinding.ActivityMainBinding
import com.hackyeon.lolscore.dialog_fragment.AlertDialogFragment
import com.hackyeon.lolscore.service.ImgRetrofitService
import com.hackyeon.lolscore.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var inputMethodManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        createListener()
    }

    private fun initView() {
        mainActivity = this

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitService = retrofit.create(RetrofitService::class.java)

        imgRetrofit = Retrofit.Builder()
            .baseUrl(DataObject.IMG_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        imgRetrofitService = imgRetrofit.create(ImgRetrofitService::class.java)

        loadChampionJson()
        loadSpellJson()

        db = SummonerDatabase.getInstance(applicationContext)!!
        inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        binding.recordRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recordRecyclerView.adapter = MainRecyclerViewAdapter(summonerList, this)

        Thread {
            var temp = db.summonerDao().getAll()
            for (i in temp) {
                summonerList.add(i)
                summonerNameList.add(i.name)
            }
            runOnUiThread {
                if (summonerList.size == 0) {
                    recordVisibilityGone()
                } else {
                    binding.recordRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }.start()

    }


    private fun createListener() {
        binding.searchEditText.setOnKeyListener { _, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_ENTER) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                searchSummoner()
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun searchSummoner() {
        retrofitService.getSummoner(binding.searchEditText.text.toString())
            .enqueue(object : Callback<SummonerData> {
                override fun onResponse(call: Call<SummonerData>, response: Response<SummonerData>) {
                    if (response.isSuccessful) {
                        var body = response.body()!!

                        searchTier(body, this@MainActivity, false)
                        binding.searchEditText.text.clear()
                    } else {
                        AlertDialogFragment("소환사 이름을 확인하세요.").show(
                            supportFragmentManager,
                            "alertDialogFragment"
                        )
                        inputMethodManager.showSoftInput(binding.searchEditText, 0)
                    }
                }

                override fun onFailure(call: Call<SummonerData>, t: Throwable) {
                    AlertDialogFragment("인터넷 연결을 확인하세요.").show(
                        supportFragmentManager,
                        "alertDialogFragment"
                    )
                }
            })
    }

    private fun loadChampionJson() {
        imgRetrofitService.getChampionImg().enqueue(object : Callback<ImgDataJson> {
            override fun onResponse(call: Call<ImgDataJson>, response: Response<ImgDataJson>) {
                if (response.isSuccessful) {

                    var data = response.body()?.data
                    var dataList = data?.keySet()

                    for(i in dataList!!){
                        var key = data?.getAsJsonObject(i)?.getAsJsonPrimitive("key")?.asInt
                        championMap[key!!] = i
                    }
                }
            }

            override fun onFailure(call: Call<ImgDataJson>, t: Throwable) {
            }
        })
    }

    private fun loadSpellJson() {
        imgRetrofitService.getSpellImg().enqueue(object : Callback<ImgDataJson> {
            override fun onResponse(call: Call<ImgDataJson>, response: Response<ImgDataJson>) {
                if (response.isSuccessful) {
                    var data = response.body()?.data
                    var dataList = data?.keySet()

                    for(i in dataList!!){
                        var key = data?.getAsJsonObject(i)?.getAsJsonPrimitive("key")?.asInt
                        spellMap[key!!] = i
                    }
                }
            }

            override fun onFailure(call: Call<ImgDataJson>, t: Throwable) {
            }
        })
    }

    fun recordVisibilityGone(){
        binding.recordRecyclerView.visibility = GONE
        binding.recordTextView.visibility = GONE
    }

    fun updateRecyclerView(){
        runOnUiThread {
            if(binding.recordRecyclerView.visibility == GONE){
                binding.recordTextView.visibility = VISIBLE
                binding.recordRecyclerView.visibility = VISIBLE
            }
            binding.recordRecyclerView.adapter?.notifyDataSetChanged()
        }
    }

}