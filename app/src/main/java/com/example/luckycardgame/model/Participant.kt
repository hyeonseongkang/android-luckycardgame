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
}