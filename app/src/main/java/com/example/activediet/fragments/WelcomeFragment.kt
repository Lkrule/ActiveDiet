package com.example.activediet.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.activediet.databinding.FragmentWelcomeBinding
import javax.inject.Inject


class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferences : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if(sharedPreferences?.getFloat(BMR_PREF, 0f) != 0f) {
//           navigateToDailyFragment()
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.welcomeNextBtn.setOnClickListener {
                val action = WelcomeFragmentDirections.actionWelcomeFragmentToCalculatorFragment()
            findNavController().navigate(action)
        }
    }


    private fun navigateToDailyFragment() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToDailyFragment()
        findNavController().navigate(action)
    }
}