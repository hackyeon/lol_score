package com.hackyeon.lolscore.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hackyeon.lolscore.R
import com.hackyeon.lolscore.adapter.DetailRecyclerViewAdapter
import com.hackyeon.lolscore.data.DataObject.championMap
import com.hackyeon.lolscore.data.DataObject.spellMap
import com.hackyeon.lolscore.data.ParticipantIdentities
import com.hackyeon.lolscore.data.Participants
import com.hackyeon.lolscore.data.Team
import com.hackyeon.lolscore.databinding.FragmentDetailBinding

class DetailFragment(
    private var team: Team,
    private var participants: MutableList<Participants>,
    private var participantIdentities: MutableList<ParticipantIdentities>
) : Fragment() {
    private lateinit var binding: FragmentDetailBinding

    private var championNameList = mutableListOf<String>()
    private var spell1NameList = mutableListOf<String>()
    private var spell2NameList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        createRecyclerView()
    }

    private fun initView(){
        if(team.win == "Win") {
            binding.resultLayout.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.win_blue_soft))
            binding.resultTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.win_blue))
            binding.resultTextView.text = "승리"
            glideIcon(R.drawable.kda_blue, binding.kdaImgView)
            glideIcon(R.drawable.nashor_blue, binding.nashorImgView)
            glideIcon(R.drawable.dragon_blue, binding.dragonImgView)
            glideIcon(R.drawable.turret_blue, binding.turretImgView)
        }
        else {
            binding.resultLayout.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lose_red_soft))
            binding.resultTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.lose_red))
            binding.resultTextView.text = "패배"
            glideIcon(R.drawable.kda_red, binding.kdaImgView)
            glideIcon(R.drawable.nashor_red, binding.nashorImgView)
            glideIcon(R.drawable.dragon_red, binding.dragonImgView)
            glideIcon(R.drawable.turret_red, binding.turretImgView)
        }

        binding.teamColorTextView.text = if(team.teamId == 100) "(블루)" else "(레드)"
        binding.nashorTextView.text = team.baronKills.toString()
        binding.dragonTextView.text = team.dragonKills.toString()
        binding.turretTextView.text = team.towerKills.toString()

        var teamKill = 0
        var teamDeath = 0
        var teamAssist = 0

        for(i in participants){
            teamKill += i.stats.kills
            teamDeath += i.stats.deaths
            teamAssist += i.stats.assists
        }
        binding.kdaTextView.text = "$teamKill / $teamDeath / $teamAssist"
    }


    private fun createRecyclerView(){
        for(i in participants){
            championNameList.add(championMap[i.championId]!!)
            spell1NameList.add(spellMap[i.spell1Id]!!)
            spell2NameList.add(spellMap[i.spell2Id]!!)
        }

        binding.recyclerView.adapter = DetailRecyclerViewAdapter(requireActivity(),
            participants,participantIdentities, championNameList, spell1NameList, spell2NameList)
    }

    private fun glideIcon(img: Int, view: AppCompatImageView){
        Glide.with(this)
            .load(img)
            .centerCrop()
            .into(view)
    }

}