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
import com.example.luckycardgame.`interface`.ResultListener
import com.example.luckycardgame.model.Card
import com.example.luckycardgame.viewmodel.LuckyBoardGameViewModel

class MainActivity : AppCompatActivity(), ResultListener {

    companion object {
        const val TAG: String = "로그"
    }
    lateinit var luckyBoardGameViewModel: LuckyBoardGameViewModel

    private val overlappingItemDecoration = CardAdapter.OverlappingItemDecoration(-10)

    var cardList = mutableListOf<Card>()

    private var mBinding: ActivityMainBinding? = null

    private val binding get() = mBinding!!

    fun <T> List<T>.combinations(): List<Pair<T, T>> {
        val combinations = mutableListOf<Pair<T, T>>()
        for (i in indices) {
            for (j in (i + 1) until size) {
                combinations.add(Pair(this[i], this[j]))
            }
        }
        return combinations
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // resultMap 예시
//        val resultMap = mutableMapOf(
//            "A" to listOf(1, 1, 1),
//            "B" to listOf(3, 3, 3),
//            "C" to listOf(2, 2, 2),
//            "D" to listOf(8, 8, 8)
//        )
//
//        val entries = resultMap.entries.toList()
//
//        for ((entry1, entry2) in entries.combinations()) {
//            val (owner1, cards1) = entry1
//            val (owner2, cards2) = entry2
//
//            // 카드 비교 및 결과 출력
//            if (cards1.all { it == cards2[0] }) {
//                Log.d("로그", "Cards of $owner1 are all the same as cards of $owner2")
//            } else {
//                Log.d("로그", "Cards of $owner1 are all the same as cards of $owner2")
//            }
//
//            // 카드 값 출력
//            Log.d("로그", "Cards of $owner1: ${cards1.joinToString(", ")}")
//            Log.d("로그", "Cards of $owner2: ${cards2.joinToString(", ")}")
//        }

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
            cardList.add(Card())
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
            luckyBoardGameViewModel.initResultData()
            var count = 0
            var cardCount: Int
            for (participant in it) {
                if (count == 0) {
                    cardCount = participant.retrieveParticipantCards().size
                    initView(cardCount)
                }

                when(count) {
                    0 -> binding.rvA.adapter = CardAdapter("A", participant.retrieveParticipantCards(), false, luckyBoardGameViewModel)
                    1 -> binding.rvB.adapter = CardAdapter("B", participant.retrieveParticipantCards(), false, luckyBoardGameViewModel)
                    2 -> binding.rvC.adapter = CardAdapter("C", participant.retrieveParticipantCards(), false, luckyBoardGameViewModel)
                    3 -> binding.rvD.adapter = CardAdapter("D", participant.retrieveParticipantCards(), false, luckyBoardGameViewModel)
                    4 -> binding.rvE.adapter = CardAdapter("E", participant.retrieveParticipantCards(), false, luckyBoardGameViewModel)
                }

                count++
            }
        }

        luckyBoardGameViewModel.tableLiveData.observe(this) {
                it ->
            if (it.retrieveTableCards().size == 6) {
                binding.rvTable.adapter = CardAdapter("T", it.retrieveTableCards(), false, luckyBoardGameViewModel)
            } else {
                binding.rvTable.adapter = CardAdapter("T", it.retrieveTableCards(), true, luckyBoardGameViewModel)
            }
        }

        luckyBoardGameViewModel.selectedCards.observe(this) { resultData ->
            resultData.forEach { resultMap ->
               // Log.d("로그", "hello!@!@")
                for ((owner, cards) in resultMap) {

                    Log.d("로그", "Owner: $owner")
                    cards.forEach { card ->
                        Log.d("로그", "3장 카드의 값이 같으므로 결과화면으로 ㄱㄱ Card: ${card.getCardNumber()} ${card.getCardTypeShape()}")
                    }
                    val firstCardNumber = cards.firstOrNull()?.getCardNumber()
//                    if (cards.all { it.getCardNumber() == firstCardNumber }) {
//                        clearAdapter(owner)
//                        cards.forEach { card ->
//                            Log.d("로그", "3장 카드의 값이 같으므로 결과화면으로 ㄱㄱ Card: ${card.getCardNumber()} ${card.getCardTypeShape()}")
//                        }
//                    }
                }
            }
        }
    }

    private fun clearAdapter(owner: String) {
        when(owner) {
            "A" ->
                // 결과 Adapter 생성
                Log.d("로그", "A Clear 후 뷰 재생성")
            "B"->
                Log.d("로그", "B Clear 후 뷰 재생성")
            "C"->
                Log.d("로그", "C Clear 후 뷰 재생성")
            "D"->
                Log.d("로그", "D Clear 후 뷰 재생성")
            "E"->
                Log.d("로그", "E Clear 후 뷰 재생성")
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

    override fun onResultDataSelected(selectedCards: List<Card>) {
        for (card in selectedCards) {
            Log.d("로그", card.getCardTypeShape() + " " + card.getCardFront() + " " + card.getCardNumber())
        }
    }
}