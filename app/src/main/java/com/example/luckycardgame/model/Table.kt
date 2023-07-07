package com.example.luckycardgame.model

class Table {
    private val tableCards: MutableList<Card> = mutableListOf()

    fun addCard(card: Card) {
        tableCards.add(card)
    }

    fun removeCard(card: Card) {
        tableCards.remove(card)
    }

    fun retrieveTableCards(): MutableList<Card> {
        return tableCards
    }

    fun clear() {
        tableCards.clear()
    }
}