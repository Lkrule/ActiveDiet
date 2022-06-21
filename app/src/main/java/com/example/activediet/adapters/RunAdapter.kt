package com.example.activediet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.activediet.databinding.RunItemBinding
import com.example.activediet.data.Run
import com.example.activediet.utilities.run.TrackingUtility
import java.text.SimpleDateFormat
import java.util.*


class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {


    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Run>(){
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }})


    inner class RunViewHolder(private val binding: RunItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(run: Run) {
            binding.apply {
                Glide.with(itemView).load(run.Image).into(RunImage)

                val calendar = Calendar.getInstance().apply {
                    timeInMillis = run.TimeStamp
                }
                val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
                Date.text = dateFormat.format(calendar.time)

                AvgSpeed.text = "${ run.AvgSpeedInKmh }km/h"
                Distance.text = "${run.DistInMeters / 1000f}km"
                Calories.text = "${run.CalBurned}kcal"

                Time.text = TrackingUtility.getFormattedStopWatchTime(run.TimeInMs)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val binding = RunItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RunViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        holder.bind(run)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Run>) = differ.submitList(list)
}