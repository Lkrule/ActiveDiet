package com.example.activediet.fragments.run

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.activediet.R
import androidx.lifecycle.Observer
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


    private val viewModel: StatisticsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupBarChart()
    }


    private fun setupBarChart() {
    }

    private fun subscribeToObservers() {
        viewModel.totalTimeInMs.observe(viewLifecycleOwner, Observer {})
        viewModel.totalDist.observe(viewLifecycleOwner, Observer {})
        viewModel.totalAvgSpeed.observe(viewLifecycleOwner, Observer {})
        viewModel.totalCalsBurned.observe(viewLifecycleOwner, Observer {})
        viewModel.runsSortedByDate.observe(viewLifecycleOwner, Observer {})
    }
}