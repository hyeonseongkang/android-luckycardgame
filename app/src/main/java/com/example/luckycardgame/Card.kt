package com.example.luckycardgame

import java.util.Random

class Card(val type: CardType, var number: Int) {
    companion object {
        private val random = Random()
    }

    constructor() : this(
        when (random.nextInt(3)) {
            0 -> DogCard
            1 -> CatCard
            else -> CowCard
        },
        random.nextInt(12) + 1
    )

    fun getCardTypeShape(): String {
        return type.shape
    }

    fun getCardNumber(): Int {
        return number
    }

    // 카드 속성 출력 함수
    fun printCardProperties() {
        val formattedNumber = if (getCardNumber() < 10) {
            "0${getCardNumber()}"
        } else {
            getCardNumber().toString()
        }
        println("${getCardTypeShape()}$formattedNumber")
    }
}
