package com.example.activediet.fragments.run

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.activediet.R
import com.example.activediet.databinding.FragmentSetupBinding
import com.example.activediet.utilities.run.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.activediet.utilities.run.Constants.KEY_NAME
import com.example.activediet.utilities.run.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment() {
    private var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var isFirstAppOpen = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetupBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val action = SetupFragmentDirections.actionSetupFragmentToRunFragment()
        

        _binding?.tvContinue?.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if (success){
                findNavController().navigate(action)
            }else{
                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_SHORT).show()
            }

        }
    }

    private fun writePersonalDataToSharedPref() : Boolean{
        return true
    }
}