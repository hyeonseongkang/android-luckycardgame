package com.example.luckycardgame.model

class Table {
    private val cards: MutableList<Card> = mutableListOf()

    fun addCard(card: Card) {
        cards.add(card)
    }

    fun removeCard(card: Card) {
        cards.remove(card)
    }

    fun getCards(): MutableList<Card> {
        return cards
    }

    fun clear() {
        cards.clear()
    }
}