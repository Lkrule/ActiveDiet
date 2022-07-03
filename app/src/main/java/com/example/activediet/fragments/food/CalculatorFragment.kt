package com.example.activediet.fragments.food

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.activediet.databinding.FragmentCalculatorBinding
import com.example.activediet.utilities.Constants
import com.example.activediet.utilities.Constants.BMR
import com.example.activediet.viewmodels.food.CalculatorViewModel
import dagger.hilt.android.AndroidEntryPoint




@AndroidEntryPoint
class CalculatorFragment : Fragment() {
    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!

    private val  viewModel: CalculatorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addOnClickListeners()

        viewModel.bmr.observe(viewLifecycleOwner) {
            binding.apply {
                calcBmi.visibility = View.VISIBLE
                calcBmi.text = viewModel.calcBMI().toString() +  "kg/m^2"
                calcYourBmi.visibility = View.VISIBLE
                applyButton.visibility = View.VISIBLE
            }
        }
    }

    private val applyButtonClickListener = View.OnClickListener {
        val bmr = viewModel.bmr.value?.toFloat()
        if (bmr == null) viewModel.sharedPrefs.edit().putFloat(BMR,  0f)
        else viewModel.sharedPrefs.edit().putFloat(BMR,  bmr)
        val action = CalculatorFragmentDirections
            .actionCalculatorFragmentToSechduleFragment()
        findNavController().navigate(action)
    }


    private val calculateButtonListener = View.OnClickListener {
        if (validate()) {
            binding.apply {
                val activity = calcActivitySpinner.selectedItemPosition
                val goal = calcGoalSpinner.selectedItemPosition
                viewModel.calcBMR(activity, goal)
            }
        }
        else Toast.makeText(context,"no settings or no plan", Toast.LENGTH_LONG).show()
    }

    private fun validate(): Boolean {
        return binding.calcActivitySpinner.selectedItemPosition > 0 &&
                binding.calcGoalSpinner.selectedItemPosition > 0 &&
                viewModel.sharedPrefs.getString(Constants.KEY_NAME, "").toString().isNotEmpty()
    }

    private fun addOnClickListeners(){
        binding.apply {
            calcButton.setOnClickListener(calculateButtonListener)
            applyButton.setOnClickListener(applyButtonClickListener)
        }
    }
}