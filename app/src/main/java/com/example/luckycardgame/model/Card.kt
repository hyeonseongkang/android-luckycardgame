package com.example.luckycardgame.model

import java.util.Random

class Card(val type: String, var number: Int, var front: Boolean) {
    companion object {
        private val random = Random()
    }

    fun getCardTypeShape(): String {
        return type
    }

    fun getCardNumber(): Int {
        return number
    }

    fun getCardFront(): Boolean {
        return front
    }

    fun setCardFront(front: Boolean) {
        this.front = front
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
