package com.example.activediet.fragments.food

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.activediet.adapters.SearchAdapter
import com.example.activediet.api.FoodAPI
import com.example.activediet.data.Nutrient
import com.example.activediet.databinding.FragmentSearchBinding
import com.example.activediet.viewmodels.food.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private var adapter: SearchAdapter = SearchAdapter(mutableListOf
        (Nutrient("food",4.0.toFloat() ,"ug")))


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rv.adapter = adapter
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.products.observe(viewLifecycleOwner) {
            if(viewModel.isTextChange) {
                val adapter = context?.let { it1 ->
                    ArrayAdapter(
                        it1,
                        R.layout.simple_list_item_1, it)
                }
                binding.autoCompleteTextView.setAdapter(adapter)
                adapter?.notifyDataSetChanged()
                viewModel.isTextChange = false
            }
        }


        viewModel.nutrients.observe(viewLifecycleOwner) {
            adapter.updateData(it)
            adapter.notifyDataSetChanged()
        }

        binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            viewModel.launch(position)
        }

        binding.autoCompleteTextView.addTextChangedListener {
            if(it?.length!! > 2) viewModel.afterTextChanged(it.toString())
        }

    }
}