package com.example.activediet.fragments.run

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.activediet.R
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.activediet.adapters.RunAdapter
import com.example.activediet.databinding.FragmentStatisticsBinding
import com.example.activediet.utilities.run.CustomMarkerView
import com.example.activediet.utilities.run.TrackingUtility
import com.example.activediet.viewmodels.run.StatisticsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StatisticsFragment : Fragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!




    private lateinit var runAdapter: RunAdapter


    private val viewModel: StatisticsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        recyclerView()

        when(viewModel.sortType) {
            "timestamp" -> binding.filter.setSelection(0)
            "time_ms" -> binding.filter.setSelection(1)
            "distance" -> binding.filter.setSelection(2)
            "speed" -> binding.filter.setSelection(3)
            "calories" -> binding.filter.setSelection(4)
        }

        binding.filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                when(pos) {
                    0 -> viewModel.sortRuns("timestamp")
                    1 -> viewModel.sortRuns("time_ms")
                    2 -> viewModel.sortRuns("distance")
                    3 -> viewModel.sortRuns("speed")
                    4 -> viewModel.sortRuns("calories")
                }
            }
        }
        return binding.root
    }

    private fun recyclerView() {
        runAdapter = RunAdapter()
        binding.runs.adapter = runAdapter
        binding.runs.layoutManager = LinearLayoutManager(requireContext())
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // setup bar chart


        binding.apply {
            barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
            }
            barChart.axisLeft.apply {
                axisLineColor = Color.WHITE
                textColor = Color.WHITE
                setDrawGridLines(false)
            }
            barChart.axisRight.apply {
                axisLineColor = Color.WHITE
                textColor = Color.WHITE
                setDrawGridLines(false)
            }
            barChart.apply {
                description.text = "Avg Speed Over Time"
                legend.isEnabled = false
            }
        }

        // other observers
        subscribeToObservers()
    }


    private fun subscribeToObservers() {
        // subscribe To Observers
        viewModel.sortBy("timestamp").observe(viewLifecycleOwner, Observer {
            it?.let {
                val allAvgSpeeds = it.indices.map { i -> BarEntry(i.toFloat(), it[i].AvgSpeedInKmh) }
                //
                val barDataSet = BarDataSet(allAvgSpeeds, "Avg Speed Over Time").apply {
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                }


                binding.apply {
                    barChart.apply {
                        data = BarData(barDataSet)
                        marker = CustomMarkerView(it.reversed(), requireContext(), R.layout.marker_view)
                        invalidate()
                    }
                }
            }
        })

        // other observers
        viewModel.runs.observe(viewLifecycleOwner) {
            runAdapter.submitList(it)
        }
        viewModel.totalTimeInMs.observe(viewLifecycleOwner) {
            it?.let {
                val totalTimeRun = TrackingUtility.getFormattedStopWatchTime(it)
                binding.totalTime.text = totalTimeRun
            }
        }
        viewModel.totalDist.observe(viewLifecycleOwner) {
            it?.let {
                val km = it / 1000f
                val totalDistance = Math.round(km * 10f) / 10f
                val totalDistanceString = "${totalDistance}km"
                binding.totalDistance.text = totalDistanceString
                binding.tvTotalDistanceInfo
            }
        }
        viewModel.totalAvgSpeed.observe(viewLifecycleOwner) {
            it?.let {
                val avgSpeed = Math.round(it * 10f) / 10f
                val avgSpeedString = "${avgSpeed}km/h"
                binding.averageSpeed.text = avgSpeedString
            }
        }
        viewModel.totalCalsBurned.observe(viewLifecycleOwner) {
            it?.let {
                val totalCalories = "${it}kcal"
                binding.totalCalories.text = totalCalories
            }
        }
    }
}