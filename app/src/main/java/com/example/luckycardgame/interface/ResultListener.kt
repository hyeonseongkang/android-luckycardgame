package com.example.luckycardgame.`interface`

import com.example.luckycardgame.model.Card

interface ResultListener {
    fun onResultDataSelected(selectedCards: List<Card>)
}