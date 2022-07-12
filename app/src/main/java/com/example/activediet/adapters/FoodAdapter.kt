package com.example.activediet.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.activediet.data.FoodSearch
import com.example.activediet.databinding.ProductItemBinding

class FoodAdapter(
    private val data: List<FoodSearch>,
    private val foodListener: FoodProductAdapter,
    private val mealIndex: Int
) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: FoodSearch) {
            binding.apply {
                productItemTitle.text = food.name.replaceFirstChar { it.uppercase() }

                productItemQuantity.text = food.let {
                    "${it.amount} ${it.unit}"
                }

                productItemKcal.text = food.nutrients.getCalories().let {
                    "${String.format("%.2f", it.amount)} ${it.unit}"
                }
                productItemFat.text = food.nutrients.getFat().let {
                    "${String.format("%.2f", it.amount)} ${it.unit}"
                }
                productItemCarbs.text = food.nutrients.getCarbs().let {
                    "${String.format("%.2f", it.amount)} ${it.unit}"
                }
                productItemProtein.text =
                    food.nutrients.getProtein().let {
                        "${String.format("%.2f", it.amount)} ${it.unit}"
                    }

                productItemRemoveBtn.setOnClickListener {
                    foodListener.onFoodRemoveClick(food, mealIndex)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface FoodProductAdapter {
        fun onFoodRemoveClick(food: FoodSearch, mealIndex: Int)
    }
}