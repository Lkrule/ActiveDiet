package com.example.activediet.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.activediet.R
import com.example.activediet.databinding.FragmentCalculatorBinding
import com.example.activediet.utilities.BMRCalculator


class CalculatorFragment : Fragment() {
    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }





    // validation for input values
    private fun validateForm(): Boolean {
        var validationFailed = false
        binding.apply {
            if (!BMRCalculator.validateGender(calcGenderSpinner.selectedItemPosition)) {
                (calcGenderSpinner.selectedView as TextView).error =
                    getString(R.string.item_required)
                validationFailed = true
            }
            if (!BMRCalculator.validateWeight(calcWeightIn.text.toString())) {
                calcWeightIn.error = getString(R.string.item_required)
                validationFailed = true
            }
            if (!BMRCalculator.validateHeight(calcHeightIn.text.toString())) {
                calcHeightIn.error = getString(R.string.item_required)
                validationFailed = true
            }
            if (!BMRCalculator.validateAge(calcAgeIn.text.toString())) {
                calcAgeIn.error = getString(R.string.item_required)
                validationFailed = true
            }
            if (!BMRCalculator.validateActivity(calcActivitySpinner.selectedItemPosition)) {
                (calcActivitySpinner.selectedView as TextView).error =
                    getString(R.string.item_required)
                validationFailed = true
            }
            if (!BMRCalculator.validateGoal(calcGoalSpinner.selectedItemPosition)) {
                (calcGoalSpinner.selectedView as TextView).error =
                    getString(R.string.item_required)
                validationFailed = true
            }
        }
        return validationFailed
    }

}