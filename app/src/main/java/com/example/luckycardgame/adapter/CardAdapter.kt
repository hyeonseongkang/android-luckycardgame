package com.example.luckycardgame.adapter

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.luckycardgame.R
import com.example.luckycardgame.model.Card
import com.example.luckycardgame.viewmodel.LuckyBoardGameViewModel

class CardAdapter(var owner: String, var cardList: MutableList<Card>, val table: Boolean, private val viewModel: LuckyBoardGameViewModel): RecyclerView.Adapter<CardAdapter.CustomViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_card_item, parent, false)
        val tableCardView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_table_card_item, parent, false)

        var viewHolder: ViewHolder
        if (table) {
            viewHolder = CustomViewHolder(tableCardView)
        } else {
            viewHolder = CustomViewHolder(view)
        }

        // 아이템 뷰의 클릭 이벤트 처리
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            // 클릭된 아이템의 위치(position)을 사용하여 필요한 작업 수행

            val currentCard = cardList[position]

            var count: Int = 0

            for (card in cardList) {
                if (card.getCardFront()) {
                    count++
                }
            }

            if (count >= 3) {
                return@setOnClickListener
            }

            if (position == 0 || position == cardList.size - 1) {
                currentCard.setCardFront(true)
                notifyDataSetChanged()
            } else  {
                // 1번째 이상의 카드를 클릭 했을 경우 (0시작)
                val prevCard = cardList[position - 1]
                var condi: Boolean = false
                if (prevCard.getCardFront()) {
                    currentCard.setCardFront(true)
                    notifyDataSetChanged()
                    condi = true
                }

                if (!condi) {
                    if (position < cardList.size - 1) {
                        // 마지막 -1 번째 카드를 클릭 했을 경우
                        val nextCard = cardList[position + 1]
                        if (nextCard.getCardFront()) {
                            currentCard.setCardFront(true)
                            notifyDataSetChanged()
                        }
                    }
                }
            }

            count = 0
            for (card in cardList) {
                if (card.getCardFront()) {
                    count++
                }
            }

            Log.d("로그", count.toString())
            if (count == 3) {
                val selectedCards = cardList.filter { it.getCardFront() }
                if (selectedCards.distinctBy { it.getCardNumber() }.size == 1) {
                    // 3장의 카드가 앞면으로 뒤집힌 상태에서 같은 숫자인지 확인
                    cardList.clear()
                    notifyDataSetChanged()
                }
                viewModel.setResultData(owner, selectedCards)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: CardAdapter.CustomViewHolder, position: Int) {
        holder.card_number_top.text = cardList.get(position).getCardNumber().toString()
        holder.card_number_bottom.text = cardList.get(position).getCardNumber().toString()
        holder.card_shape.text = cardList.get(position).getCardTypeShape()
        if (cardList.get(position).getCardFront()) {
            holder.rl_card_info.visibility = View.VISIBLE
            holder.rl_card_backgroun.visibility = View.GONE
        } else {
            holder.rl_card_info.visibility = View.GONE
            holder.rl_card_backgroun.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rl_card_info = itemView.findViewById<RelativeLayout>(R.id.rl_card_info)
        val rl_card_backgroun = itemView.findViewById<RelativeLayout>(R.id.rl_card_background)
        val card_number_top = itemView.findViewById<TextView>(R.id.tv_card_number_top)
        val card_number_bottom = itemView.findViewById<TextView>(R.id.tv_card_number_bottom)
        val card_shape = itemView.findViewById<TextView>(R.id.tv_card_shape)

    }

    class OverlappingItemDecoration(private var overlapPx: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            if (position > 0) {
                outRect.left = -overlapPx
            }
        }
    }


}