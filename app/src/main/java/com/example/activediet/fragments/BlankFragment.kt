package com.example.activediet.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        val vegetarian_button = binding.Vegetarian
        vegetarian_button.setOnClickListener {
            goToUrl("https://www.self.com/gallery/high-protein-vegetarian-meals")
        }
        val vegan_button = binding.Vegan
        vegan_button.setOnClickListener {
            goToUrl("https://www.olivemagazine.com/recipes/collection/high-protein-vegan-meals/")
        }
        val low_cal_button = binding.LowCal
        low_cal_button.setOnClickListener {
            goToUrl("https://www.bbcgoodfood.com/recipes/category/all-healthy")
        }
        val post_button = binding.appCompatButton3
        post_button.setOnClickListener {
            goToUrl("https://www.rhoderunner.com/run-club-blog/what-to-do-immediately-after-a-run")
        }
        val warmup_button = binding.appCompatButton2
        warmup_button.setOnClickListener {
            goToUrl("https://www.planetfitness.com/community/articles/easy-10-minute-cardio-warm")
        }
        val running_button = binding.appCompatButton
        running_button.setOnClickListener {
            goToUrl("https://www.nytimes.com/guides/well/how-to-start-running")
        }
        val train_button = binding.trainingButton
        train_button.setOnClickListener{
            goToUrl("https://www.nuffieldhealth.com/article/gym-workouts-for-beginners")
        }
        val health_button = binding.healthButton
        health_button.setOnClickListener{
            goToUrl("https://kaynutrition.com/healthy-daily-habits/")
        }
        val func_training_button = binding.functionalTraining
        func_training_button.setOnClickListener{
            goToUrl("https://www.webmd.com/fitness-exercise/how-to-exercise-with-functional-training")
        }
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

        return binding.root
    }

    fun goToUrl(s: String) {
        var my_url = Uri.parse(s)
        startActivity( Intent(Intent.ACTION_VIEW,my_url))
    }

}


