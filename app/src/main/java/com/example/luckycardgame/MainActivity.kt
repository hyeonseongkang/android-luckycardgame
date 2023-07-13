package com.example.luckycardgame

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.luckycardgame.adapter.CardAdapter
import com.example.luckycardgame.databinding.ActivityMainBinding
import com.example.luckycardgame.`interface`.ResultListener
import com.example.luckycardgame.model.Card
import com.example.luckycardgame.viewmodel.LuckyBoardGameViewModel

class MainActivity : AppCompatActivity() {

    lateinit var luckyBoardGameViewModel: LuckyBoardGameViewModel

    private val overlappingItemDecoration = CardAdapter.OverlappingItemDecoration(-10)

    private var mBinding: ActivityMainBinding? = null

    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        observer()
        buttonClick()
    }

    fun init() {
        luckyBoardGameViewModel = ViewModelProvider(this).get(LuckyBoardGameViewModel::class.java)
        luckyBoardGameViewModel.viewModelInit()
    }

    fun buttonClick() {
        binding.btnRestart.setOnClickListener {
            binding.tvGameEnd.visibility = View.GONE
            binding.tvGameResult.visibility = View.GONE
            binding.btnRestart.visibility = View.GONE

            binding.toggleButton.visibility = View.VISIBLE
            binding.cvTable.visibility = View.VISIBLE
            showResultAdapter()
            hideResultScreen()
            restartView()
        }

        binding.toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->

            when (checkedId) {
                R.id.btn_three_user -> {
                    if (isChecked) {
                        selectParticipants(3)
                        binding.btnThreeUser.icon =
                            ContextCompat.getDrawable(this, R.drawable.baseline_check)
                        binding.btnFourUser.isChecked = false
                        binding.btnFiveUser.isChecked = false
                    } else {
                        binding.btnThreeUser.icon = null
                    }
                }

                R.id.btn_four_user -> {
                    if (isChecked) {
                        selectParticipants(4)
                        binding.btnFourUser.icon =
                            ContextCompat.getDrawable(this, R.drawable.baseline_check)
                        binding.btnThreeUser.isChecked = false
                        binding.btnFiveUser.isChecked = false
                    } else {
                        binding.btnFourUser.icon = null
                    }
                }

                R.id.btn_five_user -> {
                    if (isChecked) {
                        selectParticipants(5)
                        binding.btnFiveUser.icon =
                            ContextCompat.getDrawable(this, R.drawable.baseline_check)
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

    fun selectParticipants(participants: Int) {
        binding.cvA.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
        binding.cvB.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
        binding.cvC.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
        binding.cvD.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
        binding.cvE.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))

        binding.cvA.visibility = if (participants >= 3) View.VISIBLE else View.GONE
        binding.cvB.visibility = if (participants >= 3) View.VISIBLE else View.GONE
        binding.cvC.visibility = if (participants >= 3) View.VISIBLE else View.GONE
        binding.cvD.visibility = if (participants >= 4) View.VISIBLE else View.INVISIBLE
        binding.cvE.visibility = if (participants >= 5) View.VISIBLE else View.GONE
        luckyBoardGameViewModel.pickCards(participants)
    }

    fun observer() {
        luckyBoardGameViewModel.participantsLiveData.observe(this) { it ->
            luckyBoardGameViewModel.initResultData()
            var count = 0
            var cardCount: Int
            for (participant in it) {
                if (count == 0) {
                    cardCount = participant.retrieveParticipantCards().size
                    initView(cardCount)
                }

                when (count) {
                    0 -> binding.rvA.adapter = CardAdapter(
                        "A",
                        participant.retrieveParticipantCards(),
                        false,
                        luckyBoardGameViewModel,
                        false
                    )

                    1 -> binding.rvB.adapter = CardAdapter(
                        "B",
                        participant.retrieveParticipantCards(),
                        false,
                        luckyBoardGameViewModel,
                        false
                    )

                    2 -> binding.rvC.adapter = CardAdapter(
                        "C",
                        participant.retrieveParticipantCards(),
                        false,
                        luckyBoardGameViewModel,
                        false
                    )

                    3 -> binding.rvD.adapter = CardAdapter(
                        "D",
                        participant.retrieveParticipantCards(),
                        false,
                        luckyBoardGameViewModel,
                        false
                    )

                    4 -> binding.rvE.adapter = CardAdapter(
                        "E",
                        participant.retrieveParticipantCards(),
                        false,
                        luckyBoardGameViewModel,
                        false
                    )
                }
                count++
            }
        }

        luckyBoardGameViewModel.tableLiveData.observe(this) { it ->
            if (it.retrieveTableCards().size == 6) {
                binding.rvTable.adapter =
                    CardAdapter("T", it.retrieveTableCards(), false, luckyBoardGameViewModel, false)
            } else {
                binding.rvTable.adapter =
                    CardAdapter("T", it.retrieveTableCards(), true, luckyBoardGameViewModel, false)
            }
        }


        luckyBoardGameViewModel.selectedCards.observe(this) { resultData ->
            resultData.forEach { resultMap ->
                Log.d("로그", "hello!@!@")

                luckyBoardGameViewModel.completeParticipants = 0
                var winners: String = ""
                for ((owner, cards) in resultMap) {
                    winners += owner + ", "
                    showWinner(owner)
                }
                showResultScreen(winners)
            }
        }

        luckyBoardGameViewModel.showResultData.observe(this) { resultData ->
            resultData.entries.forEach { entry ->
                val owner = entry.key
                val cards = entry.value
                showResultAdapter()
                showResultDataAdapter(owner, cards as MutableList<Card>)
            }
        }

        luckyBoardGameViewModel.restartLiveData.observe(this) { resultData ->
            if (resultData) {
                showRestartGameDialog(this,
                    onRestartClicked = {
                        hideResultScreen()
                        luckyBoardGameViewModel.completeParticipants = 0
                        selectParticipants(luckyBoardGameViewModel.participans)
                    },
                    onCancelClicked = {
                        hideResultScreen()
                        restartView()
                    }
                )

            }
        }
    }

    private fun hideResultScreen() {
        binding.rvResultA.visibility = View.GONE
        binding.rvResultB.visibility = View.GONE
        binding.rvResultC.visibility = View.GONE
        binding.rvResultD.visibility = View.GONE
        binding.rvResultE.visibility = View.GONE

        binding.rvResultA.adapter = null
        binding.rvResultB.adapter = null
        binding.rvResultC.adapter = null
        binding.rvResultD.adapter = null
        binding.rvResultE.adapter = null

        binding.rvResultA.removeAllViews()
        binding.rvResultB.removeAllViews()
        binding.rvResultC.removeAllViews()
        binding.rvResultD.removeAllViews()
        binding.rvResultE.removeAllViews()
    }

    private fun showResultAdapter() {
        binding.rvResultA.visibility = View.VISIBLE
        binding.rvResultB.visibility = View.VISIBLE
        binding.rvResultC.visibility = View.VISIBLE
        binding.rvResultD.visibility = View.VISIBLE
        binding.rvResultE.visibility = View.VISIBLE
    }

    private fun showResultScreen(winners: String) {
        val gameResult = winners.dropLast(2)

        showResultAdapter()

        binding.tvGameEnd.visibility = View.VISIBLE
        binding.tvGameResult.visibility = View.VISIBLE
        binding.tvGameResult.text = "이번 게임의 승자는 " + gameResult + "입니다."
        binding.btnRestart.visibility = View.VISIBLE

        binding.toggleButton.visibility = View.GONE
        binding.cvTable.visibility = View.GONE
    }

    private fun restartView() {
        luckyBoardGameViewModel.viewModelInit()
        binding.rvA.adapter = null
        binding.rvB.adapter = null
        binding.rvC.adapter = null
        binding.rvD.adapter = null
        binding.rvE.adapter = null
        binding.rvTable.adapter = null

        binding.btnThreeUser.isChecked = false
        binding.btnFourUser.isChecked = false
        binding.btnFiveUser.isChecked = false
        binding.btnThreeUser.icon = null
        binding.btnFourUser.icon = null
        binding.btnFiveUser.icon = null

        binding.cvA.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
        binding.cvB.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
        binding.cvC.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
        binding.cvD.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
        binding.cvE.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
    }

    private fun showWinner(owner: String) {
        when (owner) {
            "A" -> {
                binding.cvA.setCardBackgroundColor(ContextCompat.getColor(this, R.color.pink))
            }

            "B" -> {
                binding.cvB.setCardBackgroundColor(ContextCompat.getColor(this, R.color.pink))
            }

            "C" -> {
                binding.cvC.setCardBackgroundColor(ContextCompat.getColor(this, R.color.pink))
            }

            "D" -> {
                binding.cvD.setCardBackgroundColor(ContextCompat.getColor(this, R.color.pink))
            }

            "E" -> {
                binding.cvE.setCardBackgroundColor(ContextCompat.getColor(this, R.color.pink))
            }

        }
    }

    private fun showResultDataAdapter(owner: String, cards: MutableList<Card>) {
        when (owner) {
            "A" -> {
                binding.rvA.adapter = null
                binding.rvResultA.layoutManager = GridLayoutManager(this, cards.size)
                binding.rvResultA.adapter =
                    CardAdapter("A", cards, false, luckyBoardGameViewModel, true)
            }

            "B" -> {
                binding.rvB.adapter = null
                binding.rvResultB.layoutManager = GridLayoutManager(this, cards.size)
                binding.rvResultB.adapter =
                    CardAdapter("B", cards, false, luckyBoardGameViewModel, true)
            }

            "C" -> {
                binding.rvC.adapter = null
                binding.rvResultC.layoutManager = GridLayoutManager(this, cards.size)
                binding.rvResultC.adapter =
                    CardAdapter("C", cards, false, luckyBoardGameViewModel, true)
            }

            "D" -> {
                binding.rvD.adapter = null
                binding.rvResultD.layoutManager = GridLayoutManager(this, cards.size)
                binding.rvResultD.adapter =
                    CardAdapter("D", cards, false, luckyBoardGameViewModel, true)
            }

            "E" -> {
                binding.rvE.adapter = null
                binding.rvResultE.layoutManager = GridLayoutManager(this, cards.size)
                binding.rvResultE.adapter =
                    CardAdapter("E", cards, false, luckyBoardGameViewModel, true)
            }

        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

    fun showRestartGameDialog(
        context: Context,
        onRestartClicked: () -> Unit,
        onCancelClicked: () -> Unit
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("게임 재시작")
        dialogBuilder.setMessage("무승부\n게임을 다시 시작하시겠습니까?")
        dialogBuilder.setPositiveButton("예") { dialog, which ->
            onRestartClicked.invoke()
        }
        dialogBuilder.setNegativeButton("아니오") { dialog, which ->
            onCancelClicked.invoke()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

}