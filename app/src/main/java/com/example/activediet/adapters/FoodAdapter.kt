package com.example.activediet.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.activediet.data.Food
import com.example.activediet.databinding.FoodItemBinding

class FoodAdapter(
    private val data: List<Food>,
    private val foodListener: FoodProductAdapter,
    private val mealIndex: Int
) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: FoodItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {
            binding.apply {
                foodName.text = food.name.replaceFirstChar { it.uppercase() }

                foodAmount.text = food.let { "${it.amount} g" }
                foodCals.text = food.let { "${String.format("%.2f", it.cals)} kcal" }
                foodFats.text = food.let { "${String.format("%.2f", it.fats)} g" }
                foodCarbs.text = food.let { "${String.format("%.2f", it.carbs)} g" }
                foodProteins.text = food.let { "${String.format("%.2f", it.proteins)} g" }

                productItemRemoveBtn.setOnClickListener {
                    foodListener.onFoodRemoveClick(food, mealIndex)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface FoodProductAdapter {
        fun onFoodRemoveClick(food: Food, mealIndex: Int)
    }
}