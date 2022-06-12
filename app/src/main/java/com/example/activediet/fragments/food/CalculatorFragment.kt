package com.example.activediet.fragments.food

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.activediet.R
import com.example.activediet.databinding.FragmentCalculatorBinding
import com.example.activediet.utilities.BMRCalculator
import com.example.activediet.utilities.run.Constants.BMR_PREF
import com.example.activediet.viewmodels.food.CalculatorViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject




@AndroidEntryPoint
class CalculatorFragment : Fragment() {
    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalculatorViewModel by viewModels()

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    private var calculatedBmr: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            calcCalculateBtn.setOnClickListener(calculateButtonListener)
            calcApplyButton.setOnClickListener(applyButtonClickListener)
        }

        viewModel.bmrLiveData.observe(viewLifecycleOwner) { bmr ->
            binding.apply {
                calculatedBmr = bmr.toFloat()
                calcCalculatedBmr.visibility = View.VISIBLE
                calcCalculatedBmr.text = calculatedBmr.toString() + " ${getString(R.string.kcal)}"
                calcYourBmr.visibility = View.VISIBLE
                calcApplyButton.visibility = View.VISIBLE
            }
        }
    }

    private val applyButtonClickListener = View.OnClickListener {
        sharedPrefs.edit().putFloat(BMR_PREF, calculatedBmr).apply()
        navigateToDailyFragment()
    }

    private fun navigateToDailyFragment() {
        val action = CalculatorFragmentDirections
            .actionCalculatorFragmentToDailyFragment()
        findNavController().navigate(action)
    }

    private val calculateButtonListener = View.OnClickListener {
        if (!validateForm()) {
            binding.apply {
                viewModel.calculateBMR(
                    gender = calcGenderSpinner.selectedItemPosition,
                    weight = calcWeightIn.text.toString().toFloat(),
                    height = calcHeightIn.text.toString().toFloat(),
                    age = calcAgeIn.text.toString().toInt(),
                    activity = calcActivitySpinner.selectedItemPosition,
                    goal = calcGoalSpinner.selectedItemPosition
                )
            }
        }
    }

    private fun validateForm(): Boolean {
        var validationFailed = false
        binding.apply {
            if (!BMRCalculator.validateInt(calcGenderSpinner.selectedItemPosition)||
                !BMRCalculator.validateString(calcWeightIn.text.toString()) ||
                !BMRCalculator.validateString(calcHeightIn.text.toString()) ||
                !BMRCalculator.validateString(calcAgeIn.text.toString()) ||
                !BMRCalculator.validateInt(calcActivitySpinner.selectedItemPosition) ||
                !BMRCalculator.validateInt(calcGoalSpinner.selectedItemPosition)) {
                    validationFailed = true
                    Toast.makeText(context,"Not enough parameters.", Toast.LENGTH_LONG).show();

            }
        }
        return validationFailed
    }

}