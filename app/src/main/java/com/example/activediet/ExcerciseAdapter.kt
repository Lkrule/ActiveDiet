package com.example.activediet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExcerciseAdapter: RecyclerView.Adapter<ExcerciseAdapter.ExcerciseViewHolder>() {
    private var stdList: ArrayList<ExcerciseModel> = ArrayList()
    private var onClickItem: ((ExcerciseModel) -> Unit)? = null
    private var onClickDeleteItem: ((ExcerciseModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ExcerciseViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_item_std,parent,false)
    )
    fun setOnClickItem(callback: (ExcerciseModel) -> Unit){
        this.onClickItem = callback
    }
    fun setOnClickDeleteItem(callback:(ExcerciseModel) -> Unit){
        this.onClickDeleteItem = callback
    }
    override fun onBindViewHolder(holder: ExcerciseViewHolder, position: Int) {
        val std = stdList[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener{onClickItem?.invoke(std)}
        holder.btnDelete.setOnClickListener{onClickDeleteItem?.invoke(std)}
    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    fun addItems(items:ArrayList<ExcerciseModel>){
        this.stdList = items
        notifyDataSetChanged()
    }
    class ExcerciseViewHolder (var view: View): RecyclerView.ViewHolder(view){
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var name = view.findViewById<TextView>(R.id.tvName)
        private var details = view.findViewById<TextView>(R.id.tvDetails)
        var btnDelete = view.findViewById<TextView>(R.id.btnDelete)

        fun bindView(std: ExcerciseModel){
            id.text = std.id.toString()
            name.text = std.name
            details.text = std.details
        }
    }




}
