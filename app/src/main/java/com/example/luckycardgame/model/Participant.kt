package com.example.luckycardgame.model

class Participant {
    private val cards = mutableListOf<Card>()

    fun addCard(card: Card) {
        cards.add(card)
    }

    fun removeCard(card: Card) {
        cards.remove(card)
    }

    fun getCards(): MutableList<Card> {
        return cards
    }
}