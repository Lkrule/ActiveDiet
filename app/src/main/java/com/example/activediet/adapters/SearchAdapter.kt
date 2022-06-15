package com.example.activediet.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.activediet.data.Nutrient
import com.example.activediet.data.Run
import com.example.activediet.databinding.MealItemSearchBinding

class SearchAdapter {
    inner class SearchViewHolder(private val binding: MealItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(nutrient: Nutrient) {

        }
    }
}
