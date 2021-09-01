package com.hackyeon.lolscore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.hackyeon.lolscore.adapter.MainRecyclerViewAdapter
import com.hackyeon.lolscore.data.SummonerData
import com.hackyeon.lolscore.data.Tier
import com.hackyeon.lolscore.data.DataObject.BASE_URL
import com.hackyeon.lolscore.data.DataObject.mainActivity
import com.hackyeon.lolscore.data.DataObject.sendObjectData
import com.hackyeon.lolscore.data.database.Summoner
import com.hackyeon.lolscore.data.database.SummonerDatabase
import com.hackyeon.lolscore.databinding.ActivityMainBinding
import com.hackyeon.lolscore.dialog_fragment.AlertDialogFragment
import com.hackyeon.lolscore.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitService: RetrofitService
    private lateinit var inputMethodManager: InputMethodManager
    lateinit var db: SummonerDatabase
    var summonerList = mutableListOf<Summoner>()
    var summonerNameList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        keyboardListener()
    }

    private fun initView() {
        mainActivity = this

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitService = retrofit.create(RetrofitService::class.java)

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


    private fun keyboardListener() {
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

                        searchTier(body)
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

    private fun searchTier(body: SummonerData) {
        retrofitService.getTier(body.id).enqueue(object : Callback<Array<Tier>> {
            override fun onResponse(call: Call<Array<Tier>>, response: Response<Array<Tier>>) {
                if (response.isSuccessful) {
                    var responseTier = "Unranked"
                    var responseRank = ""
                    if (!response.body().isNullOrEmpty()) {
                        for(i in response.body()!!){
                            if(i.queueType == "RANKED_SOLO_5x5"){
                                responseTier = i.tier
                                responseRank = i.rank
                            }
                        }
                    }

                    var summoner = Summoner(body.accountId, body.name, body.profileIconId, body.summonerLevel, body.id, responseTier, responseRank)

                    sendObjectData(summoner)
                    var intent = Intent(this@MainActivity, MatchActivity::class.java)
                    startActivity(intent)

                    Thread {
                        if (summonerNameList.contains(body.name)) {
                            db.summonerDao().deleteName(body.name)
                            var idx = summonerNameList.indexOf(body.name)
                            summonerNameList.removeAt(idx)
                            summonerList.removeAt(idx)
                        }

                        if (summonerList.size >= 10) {
                            var name = summonerNameList.first()
                            summonerList.removeFirst()
                            summonerNameList.removeFirst()
                            db.summonerDao().deleteName(name)
                        }

                        db.summonerDao().insert(summoner)
                        summonerList.add(summoner)
                        summonerNameList.add(summoner.name)

                        runOnUiThread {
                            if(binding.recordRecyclerView.visibility == GONE){
                                binding.recordTextViewL.visibility = VISIBLE
                                binding.recordRecyclerView.visibility = VISIBLE
                            }
                            binding.recordRecyclerView.adapter?.notifyDataSetChanged()
                        }

                    }.start()
                }
            }

            override fun onFailure(call: Call<Array<Tier>>, t: Throwable) {
            }
        })
    }

    fun recordVisibilityGone(){
        binding.recordRecyclerView.visibility = GONE
        binding.recordTextViewL.visibility = GONE
    }

}