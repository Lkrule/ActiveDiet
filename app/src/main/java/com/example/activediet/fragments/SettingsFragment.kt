package com.example.activediet.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.activediet.databinding.FragmentSettingsBinding
import com.example.activediet.utilities.Constants.KEY_AGE
import com.example.activediet.utilities.Constants.KEY_GENDER
import com.example.activediet.utilities.Constants.KEY_HEIGHT
import com.example.activediet.utilities.Constants.KEY_NAME
import com.example.activediet.utilities.Constants.KEY_WEIGHT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPrefs: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnApplyChanges
            .setOnClickListener { applyChangesToSharedPref() }
        loadFieldsFromSharedPref()
    }


    private fun loadFieldsFromSharedPref() {
        binding.WeightText
            .setText(sharedPrefs.getFloat(KEY_WEIGHT,0f).toString())
        binding.genderSpinner
            .setSelection(sharedPrefs.getInt(KEY_GENDER,0))
        binding.HeightText
            .setText(sharedPrefs.getFloat(KEY_HEIGHT,0f).toString())
        binding.AgeText
            .setText(sharedPrefs.getFloat(KEY_AGE,0f).toString())
        binding.name
            .setText(sharedPrefs.getString(KEY_NAME,""))

    }


    private fun applyChangesToSharedPref() {
        if (!validateForm()) {
            binding.apply {
                // save
                sharedPrefs.edit()
                    .putString(KEY_NAME,name.text.toString()).apply()
                sharedPrefs.edit()
                    .putInt(KEY_GENDER,genderSpinner.selectedItemPosition).apply()
                sharedPrefs.edit()
                    .putFloat(KEY_WEIGHT,WeightText.text.toString().toFloat()).apply()
                sharedPrefs.edit()
                    .putFloat(KEY_HEIGHT,HeightText.text.toString().toFloat()).apply()
                sharedPrefs.edit()
                    .putFloat(KEY_AGE,AgeText.text.toString().toFloat()).apply()
            }
            Toast.makeText(context,"Updated!", Toast.LENGTH_LONG).show()
        }
    }

     private fun validateForm(): Boolean {
        binding.apply {
            if (!validateInt(genderSpinner.selectedItemPosition) ||
                !validateString(WeightText.text.toString()) ||
                !validateString(AgeText.text.toString()) ||
                !validateString(HeightText.text.toString()) ||
                !validateString(name.text.toString())) {
                Toast.makeText(context,"Not enough parameters.", Toast.LENGTH_LONG).show();
                return true
            }
        }
        return false
    }


    private fun validateInt(input: Int): Boolean = input > 0

    private fun validateString(input: String): Boolean = input.isNotEmpty()
}