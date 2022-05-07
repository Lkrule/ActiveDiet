package com.example.activediet.fragments.run

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.activediet.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.example.activediet.utilities.run.Constants.KEY_NAME
import com.example.activediet.utilities.run.Constants.KEY_WEIGHT

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFieldsFromSharedPref()
        binding.apply {
            btnApplyChanges.setOnClickListener {
                val success = applyChangesToSharedPref()
                if (success) {
                    Snackbar.make(view, "Saved changes", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(view, "Please fill out all the fields", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }


    private fun loadFieldsFromSharedPref() {
        val name = sharedPreferences.getString(KEY_NAME, "")
        val weight = sharedPreferences.getFloat(KEY_WEIGHT, 80f)
        binding.apply {
            etName.setText(name)
            etWeight.setText(weight.toString())
        }
    }


    private fun applyChangesToSharedPref(): Boolean {
        binding.apply {
            val nameText = etName.text.toString()
            val weightText = etWeight.text.toString()

            if (nameText.isEmpty() || weightText.isEmpty()) {
                return false
            }
            sharedPreferences.edit()
                .putString(KEY_NAME, nameText)
                .putFloat(KEY_WEIGHT, weightText.toFloat())
                .apply()
            /*val toolbarText = "Let's go $nameText"
            requireActivity().tvToolbarTitle.text = toolbarText*/
            return true
        }
    }
}