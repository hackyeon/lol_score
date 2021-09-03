package com.hackyeon.lolscore.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
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
    }

    private fun initView(){
        if(team.win == "Win") {
            binding.resultLayout.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.win_blue_soft))
            binding.resultTextView.text = "승리"
        }
        else {
            binding.resultLayout.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lose_red_soft))
            binding.resultTextView.text = "패배"
        }




        for(i in participants){
            championNameList.add(championMap[i.championId]!!)
            spell1NameList.add(spellMap[i.spell1Id]!!)
            spell2NameList.add(spellMap[i.spell2Id]!!)
        }

        binding.recyclerView.adapter = DetailRecyclerViewAdapter(requireActivity(),
            participants,participantIdentities, championNameList, spell1NameList, spell2NameList)




    }

}