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
    private val _participantsLiveData: MutableLiveData<List<Participant>> = MutableLiveData()
    private val _tableLiveData: MutableLiveData<Table>  = MutableLiveData()

    val participantsLiveData: LiveData<List<Participant>> get() = _participantsLiveData
    val tableLiveData: LiveData<Table> get() = _tableLiveData

    private val participants = mutableListOf<Participant>()
    private val table = Table()

    fun makeCards(participantCount: Int) : List<Card> {
        val cardList = mutableListOf<Card>()

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
}