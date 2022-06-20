package com.example.activediet.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.activediet.data.Nutrient
import com.example.activediet.databinding.MealItemSearchBinding

class SearchAdapter(
    private val data: MutableList<Nutrient>
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {


    inner class SearchViewHolder(private val binding: MealItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(nutrient: Nutrient) {
            binding.apply {
                itemSearchTitle.text = nutrient.name
                itemAnountUnit.text = nutrient.amount.toString() + nutrient.unit
            }
        }
    }

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = MealItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    @Override
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(data[position])
    }

    @Override
    override fun getItemCount(): Int {
        return data.size
    }


    fun updateData(news: List<Nutrient>) {
        data.clear()
        data.addAll(news)
        notifyDataSetChanged()
    }
}
