package com.example.activediet.fragments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.activediet.databinding.StepCounterBinding
import com.example.activediet.utilities.Constant
import com.example.activediet.utilities.STEPNUMBER


class StepFragment : Fragment() , SensorEventListener {

    lateinit var binding:StepCounterBinding
    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalStep = 0f
    private var previousTotalStep = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StepCounterBinding.inflate(layoutInflater)
    }
    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val button3 = binding.backToMenu
        button3.setOnClickListener{
            val action = StepFragmentDirections.actionStepFragmentToBlankFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.circularProgressBar.apply {
            setProgressWithAnimation(0f)
        }
        loadData()
        resetSteps()
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running)
            totalStep = event!!.values[0]
        val currentSteps = totalStep.toInt() - previousTotalStep.toInt()
        binding.txtStepCount.text = ("$currentSteps")

        binding.circularProgressBar.apply {
            setProgressWithAnimation(currentSteps.toFloat())
        }

    }


    fun resetSteps() {
        binding.txtStepCount.setOnLongClickListener {
            previousTotalStep = totalStep
            binding.txtStepCount.text = "0"
            binding.circularProgressBar.apply {
                setProgressWithAnimation(0f)
            }
            saveDate()
            true
        }
    }


    private fun saveDate() {
        Constant.editor(requireContext()).putFloat(STEPNUMBER, previousTotalStep).apply()
    }

    private fun loadData() {
        previousTotalStep = Constant.getSharePref(requireContext()).getFloat(STEPNUMBER, 0f)
    }



}