package com.example.activediet.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.activediet.R
import com.example.activediet.databinding.FragmentBlankBinding
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var binding: FragmentBlankBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBlankBinding.inflate(layoutInflater)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // If we want to move to other fragments from menu


        val button3 = binding.calculateNav
        button3.setOnClickListener{
            val action = BlankFragmentDirections.actionBlankFragmentToCalculatorFragment()
            findNavController().navigate(action)
        }
        val button4 = binding.searchNav
        button4.setOnClickListener{
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())
            val action = BlankFragmentDirections.actionBlankFragmentToSearchFragment(currentDate)
            findNavController().navigate(action)
        }
        val button5 = binding.dailyNav
        button5.setOnClickListener{
            val action = BlankFragmentDirections.actionBlankFragmentToDailyFragment()
            findNavController().navigate(action)
        }
        // If we want to do Hyperlinks from menu
        val workOut_link = binding.workoutId
        workOut_link.setOnClickListener {printText()}

        return binding.root
    }

    public fun printText() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse
                    ("https://www.masterclass.com/articles/full-body-workout-plan#how-to-do-a-fullbody-workout")
            )
        )
    }
}


