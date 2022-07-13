package com.example.activediet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.activediet.data.Meal
import com.example.activediet.databinding.MealItemBinding

class MealsAdapter(
    private val meals: MutableList<Meal>,
    private val listener: MealsAdapterListener
) : RecyclerView.Adapter<MealsAdapter.ViewHolder>() {

    val viewHolders = mutableListOf<ViewHolder>()

    inner class ViewHolder(val binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(meal : Meal, pos: Int) {
            binding.title.text = meal.name
            binding.apply {
                addBtn.setOnClickListener {
                    listener.addBtnClicked(pos)
                }
                // layout click listener
                root.setOnClickListener {
                    if (rv.isVisible) rv.visibility = View.GONE
                    else rv.visibility = View.VISIBLE
                }
                kcal.text = String.format("%.1f", meal.cals) + " kcal"
                fat.text =  String.format("%.1f", meal.fats) + " g"
                protein.text = String.format("%.1f", meal.proteins) + " g"
                carbs.text = String.format("%.1f", meal.carbs) + " g"
            }

            viewHolders.add(this)
            listener.viewHolderBind(pos, this)
        }

        // update total of all meals
        @SuppressLint("SetTextI18n")
        fun updateMeals(meal: Meal) {
            binding.apply {
                kcal.text = String.format("%.1f", meal.cals) + " kcal"
                fat.text =  String.format("%.1f", meal.fats) + " g"
                protein.text = String.format("%.1f", meal.proteins) + " g"
                carbs.text = String.format("%.1f", meal.carbs) + " g"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(meals[position], position)
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    fun getItems(): List<Meal> {
        return meals
    }


    interface MealsAdapterListener {
        fun addBtnClicked(pos: Int)
        fun viewHolderBind(pos: Int, holder: ViewHolder)
    }
}