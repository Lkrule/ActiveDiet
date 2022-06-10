package com.example.activediet.fragments.food

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.activediet.databinding.FragmentAddCustomFoodBinding
import com.example.activediet.viewmodels.food.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddCustomFoodFragment : Fragment() {
    private var _binding: FragmentAddCustomFoodBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private val args: AddCustomFoodFragmentArgs by navArgs()

    @Inject
    lateinit var api: FoodAPI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCustomFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            button.setOnClickListener(buttonListener)
        }
    }


    // listeners
    private val buttonListener = View.OnClickListener {
        if (!validateForm()) {
            MaterialDialog(requireContext()).show {
                title(R.string.add_ingredient_dialog_title)
                // create list
                val listOfValues = Nutrients(listOf(
                    Nutrient("Fat",binding.FatAmount
                    .text.toString().toFloat(),"g"),
                    Nutrient("Protein",binding.ProteinAmount
                        .text.toString().toFloat(),"g"),
                    Nutrient("Carbohydrates",binding.CarbonAmount
                        .text.toString().toFloat(),"g"),
                    Nutrient("Calories",binding.CaloriesAmount
                        .text.toString().toFloat(),"kcal")))

                val ingredient = IngredientSearch(0,"",0,0,binding.FoodName
                    .text.toString(),"",100.toFloat(),"g",listOfValues)
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

    // validate

    private fun validateForm(): Boolean {
        var validationFailed = false
        binding.apply {
            if (!validateString(FoodName.text.toString())||
                !validateFloat(ProteinAmount.text.toString().toFloat()) ||
                !validateFloat(FatAmount.text.toString().toFloat()) ||
                !validateFloat(CarbonAmount.text.toString().toFloat()) ||
                !validateFloat(CaloriesAmount.text.toString().toFloat())) {
                validationFailed = true
                Toast.makeText(context,"Not enough parameters.", Toast.LENGTH_LONG).show();

            }
        }
        return validationFailed
    }

    private fun validateFloat(input: Float): Boolean = input > 0

    private fun validateString(input: String): Boolean = input.isNotEmpty()
}