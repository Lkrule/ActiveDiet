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
import com.example.activediet.R
import com.example.activediet.databinding.FragmentCalculatorBinding
import com.example.activediet.utilities.Constants
import com.example.activediet.utilities.Constants.BMR_PREF
import com.example.activediet.viewmodels.food.CalculatorViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject




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

        viewModel.bmrLiveData.observe(viewLifecycleOwner) { bmr ->
            binding.apply {
                calcBmi.visibility = View.VISIBLE
                calcBmi.text = viewModel.calcBMI().toString() +  "kg/m^2"
                calcYourBmi.visibility = View.VISIBLE
                applyButton.visibility = View.VISIBLE
            }
        }
    }

    private val applyButtonClickListener = View.OnClickListener {
        viewModel.sharedPrefs.edit().putFloat(BMR_PREF, 0f)
        navigateToDailyFragment()
    }

    private fun navigateToDailyFragment() {
        val action = CalculatorFragmentDirections
            .actionCalculatorFragmentToSechduleFragment()
        findNavController().navigate(action)
    }

    private val calculateButtonListener = View.OnClickListener {
        if (validate()) {
            binding.apply {
                viewModel.calcBMR(
                    activity = calcActivitySpinner.selectedItemPosition,
                    goal = calcGoalSpinner.selectedItemPosition
                )
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