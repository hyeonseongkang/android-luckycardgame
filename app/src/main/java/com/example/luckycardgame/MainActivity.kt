package com.example.luckycardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.luckycardgame.adapter.CardAdapter
import com.example.luckycardgame.databinding.ActivityMainBinding
import com.example.luckycardgame.model.Card
import com.example.luckycardgame.viewmodel.LuckyBoardGameViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG: String = "로그"
    }
    lateinit var luckyBoardGameViewModel: LuckyBoardGameViewModel

    private val overlappingItemDecoration = CardAdapter.OverlappingItemDecoration(-10)

    var cardList = mutableListOf<Card>()

    private var mBinding: ActivityMainBinding? = null

    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initCardList()
        observer()
        toggleButtonClick()
    }

    fun init() {
        luckyBoardGameViewModel = ViewModelProvider(this).get(LuckyBoardGameViewModel::class.java)
    }

    fun toggleButtonClick() {
        binding.toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->

            when (checkedId) {
                R.id.btn_three_user -> {
                    if (isChecked) {
                        selectParticipants(3)
                        binding.btnThreeUser.icon = ContextCompat.getDrawable(this, R.drawable.baseline_check)
                        binding.btnFourUser.isChecked = false
                        binding.btnFiveUser.isChecked = false
                    } else {
                        binding.btnThreeUser.icon = null
                    }
                }
                R.id.btn_four_user -> {
                    if (isChecked) {
                        selectParticipants(4)
                        binding.btnFourUser.icon = ContextCompat.getDrawable(this, R.drawable.baseline_check)
                        binding.btnThreeUser.isChecked = false
                        binding.btnFiveUser.isChecked = false
                    } else {
                        binding.btnFourUser.icon = null
                    }
                }

                R.id.btn_five_user -> {
                    if (isChecked) {
                        selectParticipants(5)
                        binding.btnFiveUser.icon = ContextCompat.getDrawable(this, R.drawable.baseline_check)
                        binding.btnThreeUser.isChecked = false
                        binding.btnFourUser.isChecked = false
                    } else {
                        binding.btnFiveUser.icon = null
                    }
                }
            }
        }
    }

    fun initView(cardCount: Int) {

        binding.rvA.layoutManager = GridLayoutManager(this, cardCount)
        binding.rvA.setHasFixedSize(true)

        binding.rvB.layoutManager = GridLayoutManager(this, cardCount)
        binding.rvB.setHasFixedSize(true)

        binding.rvC.layoutManager = GridLayoutManager(this, cardCount)
        binding.rvC.setHasFixedSize(true)

        binding.rvD.layoutManager = GridLayoutManager(this, cardCount)
        binding.rvD.setHasFixedSize(true)

        binding.rvE.layoutManager = GridLayoutManager(this, cardCount)
        binding.rvE.setHasFixedSize(true)


        if (cardCount > 6) {
            val itemCount = binding.rvA.itemDecorationCount
            if (itemCount == 0) {
                binding.rvA.addItemDecoration(overlappingItemDecoration)
                binding.rvB.addItemDecoration(overlappingItemDecoration)
                binding.rvC.addItemDecoration(overlappingItemDecoration)
                binding.rvD.addItemDecoration(overlappingItemDecoration)
                binding.rvE.addItemDecoration(overlappingItemDecoration)
            }
        } else {
            binding.rvA.removeItemDecoration(overlappingItemDecoration)
            binding.rvB.removeItemDecoration(overlappingItemDecoration)
            binding.rvC.removeItemDecoration(overlappingItemDecoration)
            binding.rvD.removeItemDecoration(overlappingItemDecoration)
            binding.rvE.removeItemDecoration(overlappingItemDecoration)
        }

        val layoutManager: RecyclerView.LayoutManager
        if (cardCount == 6) {
            layoutManager = GridLayoutManager(this, 6)
        } else if (cardCount == 7) {
            layoutManager = GridLayoutManager(this, 4)
        } else {
            layoutManager = GridLayoutManager(this, 5)
        }

        binding.rvTable.layoutManager = layoutManager
    }

    fun initCardList() {
        for (i in 0 until 12) {
            //  cardList.add(Card())
        }

    }

    fun selectParticipants(participants: Int) {
        binding.cvA.visibility = if (participants >= 3) View.VISIBLE else View.GONE
        binding.cvB.visibility = if (participants >= 3) View.VISIBLE else View.GONE
        binding.cvC.visibility = if (participants >= 3) View.VISIBLE else View.GONE
        binding.cvD.visibility = if (participants >= 4) View.VISIBLE else View.INVISIBLE
        binding.cvE.visibility = if (participants >= 5) View.VISIBLE else View.GONE
        luckyBoardGameViewModel.pickCards(participants)
    }

    fun observer() {
        luckyBoardGameViewModel.participantsLiveData.observe(this) {
                it ->
            var count = 0
            var cardCount: Int
            for (participant in it) {
                if (count == 0) {
                    cardCount = participant.retrieveParticipantCards().size
                    initView(cardCount)
                }

                when(count) {
                    0 -> binding.rvA.adapter = CardAdapter(participant.retrieveParticipantCards(), false)
                    1 -> binding.rvB.adapter = CardAdapter(participant.retrieveParticipantCards(), false)
                    2 -> binding.rvC.adapter = CardAdapter(participant.retrieveParticipantCards(), false)
                    3 -> binding.rvD.adapter = CardAdapter(participant.retrieveParticipantCards(), false)
                    4 -> binding.rvE.adapter = CardAdapter(participant.retrieveParticipantCards(), false)
                }

                for (card in participant.retrieveParticipantCards()) {
                    Log.d(TAG, "Main $count - ${card.getCardNumber()} ${card.getCardTypeShape()}")
                }
                count++
            }
        }

        luckyBoardGameViewModel.tableLiveData.observe(this) {
                it ->
            var count = 0
            if (it.retrieveTableCards().size == 6) {
                binding.rvTable.adapter = CardAdapter(it.retrieveTableCards(), false)
            } else {
                binding.rvTable.adapter = CardAdapter(it.retrieveTableCards(), true)
            }

            for (card in it.retrieveTableCards()) {
                Log.d(TAG, "Main $count - ${card.getCardNumber()} ${card.getCardTypeShape()}")
                count++
            }
            count = 0
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}