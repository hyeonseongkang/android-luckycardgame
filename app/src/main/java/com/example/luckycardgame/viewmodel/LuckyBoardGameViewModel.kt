package com.example.luckycardgame.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.luckycardgame.model.Card
import com.example.luckycardgame.model.Participant
import com.example.luckycardgame.model.Table

class LuckyBoardGameViewModel : ViewModel() {

    companion object {
        const val TAG: String = "로그"
    }

    var participans: Int = 0

    private val _resultData = MutableLiveData<MutableList<Map<String, List<Card>>>>()

    val selectedCards: MutableLiveData<MutableList<Map<String, List<Card>>>>
        get() = _resultData

    val tempResultData: MutableList<Map<String, List<Card>>> = mutableListOf()

    fun initResultData() {
        _resultData.value?.clear()
    }

    private val _participantsLiveData: MutableLiveData<List<Participant>> = MutableLiveData()
    private val _tableLiveData: MutableLiveData<Table> = MutableLiveData()

    val participantsLiveData: LiveData<List<Participant>> get() = _participantsLiveData
    val tableLiveData: LiveData<Table> get() = _tableLiveData

    private val participants = mutableListOf<Participant>()
    private val table = Table()

    fun makeCards(participantCount: Int): List<Card> {
        val cardList = mutableListOf<Card>()
        this.participans = participantCount

        // 카드 인스턴스화 및 cardList에 저장 (1-12, type: 🐶)
        for (i in 1..12) {

            if (participantCount == 3 && i == 12) continue
            val card = Card("🐶", i, false)
            cardList.add(card)
        }

        // 카드 인스턴스화 및 cardList에 저장 (13-24, type: 🐱)
        for (i in 1..12) {
            if (participantCount == 3 && i == 12) continue
            val card = Card("🐱", i, false)
            cardList.add(card)
        }

        // 카드 인스턴스화 및 cardList에 저장 (25-36, type: 🐮)
        for (i in 1..12) {
            if (participantCount == 3 && i == 12) continue
            val card = Card("🐮", i, false)
            cardList.add(card)
        }

        // cardList 랜덤하게 섞기
        cardList.shuffle()
        return cardList
    }

    fun pickCards(participantCount: Int) {
        participants.clear()
        table.clear()

        val pickedCards = makeCards(participantCount)
        val cardsPerParticipant = when (participantCount) {
            3 -> 8
            4 -> 7
            5 -> 6
            else -> throw IllegalArgumentException("Invalid participant count")
        }

        for (i in 0 until participantCount) {
            val participant = Participant()
            val startIndex = i * cardsPerParticipant
            val endIndex = startIndex + cardsPerParticipant

            for (j in startIndex until endIndex) {
                participant.addCard(pickedCards[j])
            }
            participant.sortCardsByNumber()
            participants.add(participant)
        }

        for (i in participantCount * cardsPerParticipant until pickedCards.size) {
            table.addCard(pickedCards[i])
        }
        table.sortCardsByNumber()

        _participantsLiveData.value = participants.toList()
        _tableLiveData.value = table
    }

    fun <T> List<T>.combinations(): List<Pair<T, T>> {
        val combinations = mutableListOf<Pair<T, T>>()
        for (i in indices) {
            for (j in (i + 1) until size) {
                combinations.add(Pair(this[i], this[j]))
            }
        }
        return combinations
    }

    fun setResultData(owner: String, cards: List<Card>) {
        val map = mutableMapOf<String, List<Card>>()
        map[owner] = cards

        val uniqueResultData = mutableSetOf<Map<String, List<Card>>>()

        val resultMap = mutableMapOf<String, List<Card>>()
        val currentData = _resultData.value ?: mutableListOf()

        val allCardsAreSeven = cards.all { it.getCardNumber() == 7 }
        if (allCardsAreSeven) {
            resultMap[owner] = cards
            uniqueResultData.add(resultMap)
            currentData.addAll(uniqueResultData)
            _resultData.value = currentData
            return
        }

        tempResultData.add(map)
        val entries = tempResultData.map { it.entries.first() }.toList()

        if (tempResultData.size == participans) {
            for ((entry1, entry2) in entries.combinations()) {
                val (owner1, cards1) = entry1
                val (owner2, cards2) = entry2

                val cards1Numbers = cards1.map { it.getCardNumber() }
                val cards2Numbers = cards2.map { it.getCardNumber() }

                // 각 자리수의 합과 차 계산
                val sumOfDigits =
                    cards1Numbers.zip(cards2Numbers).map { (num1, num2) -> num1 + num2 }
                val diffOfDigits =
                    cards1Numbers.zip(cards2Numbers).map { (num1, num2) -> Math.abs(num1 - num2) }

                // 각 자리수의 합 또는 차가 7인지 확인
                val isSumOfDigitsSeven = sumOfDigits.all { it == 7 }
                val isDiffOfDigitsSeven = diffOfDigits.all { it == 7 }

                if (isSumOfDigitsSeven || isDiffOfDigitsSeven) {
                    resultMap[owner1] = cards1
                    resultMap[owner2] = cards2

                    if (!uniqueResultData.any { it == resultMap }) {
                        uniqueResultData.add(resultMap)
                    }
                }
            }
            currentData.addAll(uniqueResultData)
            _resultData.value = currentData
        }
    }

    fun removeResultData(owner: String) {
        val currentData = _resultData.value ?: return
        val newData = currentData.filterNot { it.containsKey(owner) }
        _resultData.value = newData.toMutableList()
    }
}