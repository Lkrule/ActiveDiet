package com.example.activediet.fragments.food

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.example.activediet.R
import com.example.activediet.api.FoodAPI
import com.example.activediet.data.IngredientSearch
import com.example.activediet.data.Nutrient
import com.example.activediet.data.Nutrients
import com.example.activediet.databinding.FragmentSearchBinding
import com.example.activediet.viewmodels.food.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private val args: SearchFragmentArgs by navArgs()

    @Inject
    lateinit var api: FoodAPI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            button.setOnClickListener(buttonListener)
        }
        // val recyclerView = binding.searchRv

    }


    // listener
    private val buttonListener = View.OnClickListener {
        MaterialDialog(requireContext()).show {
            title(R.string.add_ingredient_dialog_title)
            var test = Nutrients(listOf(Nutrient("Fat",40.toFloat(),"g"),
                Nutrient("Protein",40.toFloat(),"g"),
                Nutrient("Carbohydrates",10.toFloat(),"g"),
                Nutrient("Calories",10.toFloat(),"kcal")))
            var ingredient = IngredientSearch(0,"",0,0,"food","",100.toFloat(),"g",test)
            input(
                hintRes = R.string.amount,
                inputType = InputType.TYPE_CLASS_NUMBER
            ) { dialog,
                text ->
                viewModel.addIngredient(args.mealID, args.date, ingredient, text.toString().toInt())
            }
            positiveButton(R.string.add)
            negativeButton(R.string.cancel)
        }
    }
}