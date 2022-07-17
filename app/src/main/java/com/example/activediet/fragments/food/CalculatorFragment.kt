package com.example.activediet.fragments.food

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.activediet.R
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
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addOnClickListeners()

        viewModel.bmr.observe(viewLifecycleOwner) {
            viewModel.sharedPrefs.edit().putFloat(BMR,  it.toFloat()).apply()
        }
    }

    private val applyBtnClickListener = View.OnClickListener {
        if (validate()) {
            binding.apply {
                val activity = calcActivitySpinner.selectedItemPosition
                val goal = calcGoalSpinner.selectedItemPosition
                viewModel.calcBMR(activity, goal)
            }
            val action = CalculatorFragmentDirections
                .actionCalculatorFragmentToSechduleFragment()
            findNavController().navigate(action)
        }
        else Toast.makeText(context,"no settings or no plan", Toast.LENGTH_LONG).show()
    }


    @SuppressLint("SetTextI18n")
    private val calcBtnListener = View.OnClickListener {
        binding.apply {
            calcBmi.visibility = View.VISIBLE
            calcBmi.text = viewModel.calcBMI().toString() + "kg/m^2"
            calcYourBmi.visibility = View.VISIBLE
            applyButton.visibility = View.VISIBLE
        }
    }

    private fun validate(): Boolean {
        return binding.calcActivitySpinner.selectedItemPosition > 0 &&
                binding.calcGoalSpinner.selectedItemPosition > 0 &&
                viewModel.sharedPrefs.getString(Constants.KEY_NAME, "").toString().isNotEmpty()
    }

    private fun addOnClickListeners(){
        binding.apply {
            calcButton.setOnClickListener(calcBtnListener)
            applyButton.setOnClickListener(applyBtnClickListener)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_settings, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                val action = CalculatorFragmentDirections
                    .actionCalculatorFragmentToSettingsFragment()
                findNavController().navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}