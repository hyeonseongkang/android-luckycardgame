package com.example.luckycardgame.adapter

import android.graphics.Rect
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.luckycardgame.R
import com.example.luckycardgame.model.Card

class CardAdapter(var cardList: MutableList<Card>, val cardOwner: Boolean, val table: Boolean): RecyclerView.Adapter<CardAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_card_item, parent, false)
        val tableCardView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_table_card_item, parent, false)
        if (table) {
            return CustomViewHolder(tableCardView)
        } else {
            return CustomViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: CardAdapter.CustomViewHolder, position: Int) {
        holder.card_number_top.text = cardList.get(position).getCardNumber().toString()
        holder.card_number_bottom.text = cardList.get(position).getCardNumber().toString()
        holder.card_shape.text = cardList.get(position).getCardTypeShape()
        if (cardOwner == true) {
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