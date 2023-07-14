package com.example.luckycardgame.model

class Participant {
    private val participantCards = mutableListOf<Card>()

    fun addCard(card: Card) {
        participantCards.add(card)
    }

    fun removeCard(card: Card) {
        participantCards.remove(card)
    }

    fun retrieveParticipantCards(): MutableList<Card> {
        return participantCards
    }

    fun sortCardsByNumber() {
        participantCards.sortBy { it.number }
    }

    fun hasThreeOfSameNumber(): Boolean {
        val cardGroups = participantCards.groupBy { it.type }
        return cardGroups.any { it.value.size >= 3 }
    }
}