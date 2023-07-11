package com.example.luckycardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    var cardList = mutableListOf<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initCardList()
        printCardList()
    }

    fun initCardList() {
        for (i in 0 until 12) {
            cardList.add(Card())
        }

    }

    fun printCardList() {
        var result: String = ""
        for (i in 0 until 12) {
            val formattedNumber = String.format("%02d", cardList.get(i).getCardNumber())
            result += cardList.get(i).getCardTypeShape() + formattedNumber + ", "
        }
        result = result.dropLast(2)
        println(result)
    }
}