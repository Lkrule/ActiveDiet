package com.example.activediet.fragments.food

import android.R
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.activediet.adapters.SearchAdapter
import com.example.activediet.api.FoodAPI
import com.example.activediet.databinding.FragmentSearchBinding
import com.example.activediet.viewmodels.food.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var adapter: SearchAdapter

    @Inject
    lateinit var api: FoodAPI


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.products.observe(viewLifecycleOwner) {
            val adapter = context?.let { it1 ->
                ArrayAdapter<String>(
                    it1,
                    R.layout.simple_list_item_1, it)
            }
            binding.autoCompleteTextView.setAdapter(adapter)
            adapter?.notifyDataSetChanged()
            viewModel.isTextChange = false
        }

        binding.autoCompleteTextView.addTextChangedListener {
            if(it?.length!! > 2) viewModel.afterTextChanged(it.toString())
        }

    }
}