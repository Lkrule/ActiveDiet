package com.example.activediet.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.activediet.ExcerciseAdapter
import com.example.activediet.ExcerciseModel
import com.example.activediet.SQLiteHelper
import com.example.activediet.databinding.FragmentDbBinding

class DbFragment: Fragment() {
    private lateinit var edName: EditText
    private lateinit var edDetails: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button
    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: ExcerciseAdapter? = null
    private var std: ExcerciseModel? = null
    private lateinit var binding: FragmentDbBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDbBinding.inflate(layoutInflater)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDbBinding.inflate(layoutInflater)
        initView()
        initRecyclerView()
        sqliteHelper = activity?.let { SQLiteHelper(it) }!!
        btnAdd.setOnClickListener{(addExcercise())}
        btnView.setOnClickListener{getExcercises()}
        btnUpdate.setOnClickListener{updateExercise()}
        adapter?.setOnClickItem {
            Toast.makeText(context,it.name,Toast.LENGTH_SHORT).show()
            edName.setText(it.name)
            edDetails.setText(it.details)
            std = it
        }
        adapter?.setOnClickDeleteItem {
            deleteExercise(it.id)
        }
        return binding.root
    }
    private fun deleteExercise(id:Int){
        if(id == null) return
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Are you sure you want to delete item?")
        builder.setCancelable(true)
        builder.setPositiveButton("yes"){dialog, _->
            sqliteHelper.deleteExerciseById(id)
            getExcercises()
            dialog.dismiss()
        }
        builder.setNegativeButton("no"){dialog, _->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }
    private fun getExcercises(){
        val stdList = sqliteHelper.getAllExcercises()
        Log.e("12345", "${stdList.size}")
        adapter?.addItems(stdList)

    }
    private fun updateExercise(){
        val name = edName.text.toString()
        val details = edDetails.text.toString()
        if (name == std?.name && details == std?.details){
            Toast.makeText(context, "Record didn't change", Toast.LENGTH_SHORT).show()
            return
        }

        if (std == null) return

        val std = ExcerciseModel(id =std!!.id, name = name, details = details)
        val status = sqliteHelper.updateExercise(std)
        if (status > -1){
           clearEditText()
            getExcercises()
        }else{
            Toast.makeText(context,"Update failed", Toast.LENGTH_SHORT).show()
        }
    }
    private fun addExcercise(){
        val name = edName.text.toString()
        val details = edDetails.text.toString()
        if (name.isEmpty() || details.isEmpty()){
            // change to toast
            Toast.makeText(context,"Please enter required field", Toast.LENGTH_SHORT).show()
        }else{
            val std = ExcerciseModel(name = name, details = details)
            val status = sqliteHelper.insertExcercise(std)
            // check insert success
            if (status > -1) {
                Toast.makeText(context,"Exercise added!", Toast.LENGTH_SHORT).show()
                clearEditText()
                getExcercises()
            } else{
                //make toast
                Toast.makeText(context,"Failed to add exercise", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = ExcerciseAdapter()
        recyclerView.adapter = adapter
    }
    private fun initView(){
        edName = binding.edName
        edDetails = binding.edDetails
        btnAdd = binding.btnAdd
        btnView = binding.btnView
        btnUpdate = binding.btnUpdate

        recyclerView = binding.recyclerView
    }
    private fun clearEditText(){
        edName.setText("")
        edDetails.setText("")
        edName.requestFocus()

    }

}
