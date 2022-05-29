package com.example.activediet.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.activediet.MainActivity
import com.example.activediet.databinding.FragmentBlankBinding
import com.example.activediet.utilities.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*



class BlankFragment : Fragment() {
    private lateinit var binding: FragmentBlankBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBlankBinding.inflate(layoutInflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlankBinding.inflate(layoutInflater)
        val HOUR_TO_SHOW_PUSH = 9
        val alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmPendingIntent by lazy{
            val intent = Intent(context, AlarmReceiver::class.java)
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }
        
        // The hour we want to receive the notification daily
        schedulePushNotifications(HOUR_TO_SHOW_PUSH, alarmManager, alarmPendingIntent)
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

        return binding.root
    }
    fun schedulePushNotifications(
        HOUR_TO_SHOW_PUSH: Int,
        alarmManager: AlarmManager,
        alarmPendingIntent: PendingIntent
    ) {
        val calendar = GregorianCalendar.getInstance().apply {
            if (get(Calendar.HOUR_OF_DAY) >= HOUR_TO_SHOW_PUSH) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
            set(Calendar.HOUR_OF_DAY, HOUR_TO_SHOW_PUSH)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmPendingIntent
        )
    }
    fun goToUrl(s: String) {
        val my_url = Uri.parse(s)
        startActivity( Intent(Intent.ACTION_VIEW,my_url))
    }
}


