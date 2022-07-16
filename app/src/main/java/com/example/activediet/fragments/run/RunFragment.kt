package com.example.activediet.fragments.run

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.activediet.databinding.FragmentRunBinding
import com.example.activediet.services.TrackingService
import com.example.activediet.utilities.Constants
import com.example.activediet.utilities.Constants.MAP_ZOOM
import com.example.activediet.utilities.Constants.PAUSE_SERVICE
import com.example.activediet.utilities.Constants.START_OR_RESUME_SERVICE
import com.example.activediet.utilities.Constants.STOP_SERVICE
import com.example.activediet.utilities.run.TimeFormatter
import com.example.activediet.utilities.track
import com.example.activediet.viewmodels.run.RunViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class RunFragment : Fragment() {

    private var _binding: FragmentRunBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RunViewModel by viewModels()
    private var isTracking = false
    private var tracks = mutableListOf<track>()

    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private var map: GoogleMap? = null

    private var time = 0L



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // map
        initMap(savedInstanceState)

        // listeners
        setOnClickListeners()

        // subscribe observers
        subscribeToObservers()
    }



    // add tracks

    private fun addLatestTrack(){
        // check path points
        if(tracks.isNotEmpty() && tracks.last().size > 1){

            val preLastLatLng = tracks.last()[tracks.last().size - 2]
            val lastLatLng = tracks.last().last()

            // track options
            val trackOptions = PolylineOptions()
                .color(Color.RED)
                .width(8F)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(trackOptions)

        }
    }

    //update track
    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if(!isTracking) {
            binding.btnToggleRun.text = "Start"
            binding.btnFinishRun.visibility = View.VISIBLE
        } else {
            binding.btnToggleRun.text = "Stop"
            binding.btnFinishRun.visibility = View.GONE
        }
    }


    private fun moveCameraToUser() {
        if(tracks.isNotEmpty() && tracks.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    tracks.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }



    // zoom track
    private fun zoomTrack() : Int {
        val bounce = LatLngBounds.builder()
        var counter = 0
        for (track in tracks){
            for (pos in track){
                counter++
                bounce.include(pos)
            }
        }
        if (counter > 0) map?.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounce.build(),
                    binding.mapView.width,
                    binding.mapView.height,
                    (binding.mapView.height * 0.2).toInt()
                )
            )
        return counter
    }



    // subscribeToObservers

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner) {
            updateTracking(it)
        }
        TrackingService.tracks.observe(viewLifecycleOwner) {
            tracks = it
            addLatestTrack()
            moveCameraToUser()
        }
        TrackingService.timeRun.observe(viewLifecycleOwner) {
            time = it
            val formattedTime = TimeFormatter.formatTime(time, true)
            binding.timer!!.text = formattedTime
        }
    }


    // save db

    private fun insertRun(){

        map?.snapshot { bitmap ->
            val weight = sharedPrefs.getFloat(Constants.KEY_WEIGHT, 0f).toString().toFloat()
            viewModel.insertRun(bitmap, tracks, weight, time)
            Toast.makeText(context,"Run Save successfully", Toast.LENGTH_SHORT).show()
            stopRun()
        }
    }


    // init map
    @SuppressLint("MissingPermission")
    private fun initMap(savedInstanceState: Bundle?){
        val frag = this

        binding.apply {

            mapView.apply {
                onCreate(savedInstanceState)
                getMapAsync {
                    if (requestPermissions()) {
                        it.isMyLocationEnabled = true
                        it.uiSettings.isMyLocationButtonEnabled = true
                        it.uiSettings.isZoomControlsEnabled = true
                    }
                    else {
                        EasyPermissions.requestPermissions(
                            frag,
                            "no gps permissions .",
                            0,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    }
                    map = it
                    // init
                    for(track in tracks) {
                        val trackOptions = PolylineOptions()
                            .color(Color.RED)
                            .width(8F)
                            .addAll(track)
                        map?.addPolyline(trackOptions)
                    }
                }
            }
        }
    }



    // map android

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }
    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }
    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }
    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }


    // listeners

    private fun setOnClickListeners(){
        binding.apply{
            btnToggleRun.setOnClickListener {
                if(isTracking) sendCommandToService(PAUSE_SERVICE)
                else sendCommandToService(START_OR_RESUME_SERVICE)
            }
            btnFinishRun.setOnClickListener {
                val test = zoomTrack()
                // check if not empty run
                if (test > 0)
                    insertRun()
            }

            btnCancelRun?.setOnClickListener {
                sendCommandToService(STOP_SERVICE)
            }
        }
    }


    // send commands

    private fun stopRun(){
        sendCommandToService(STOP_SERVICE)
        time = 0
        binding.timer!!.text = TimeFormatter.formatTime(time, true)
    }


    private fun sendCommandToService(action: String) =
        Intent(context, TrackingService::class.java).also {
            it.action = action
            context?.startService(it)
        }


    private fun requestPermissions(): Boolean {

        val context = context ?: return false

        return PackageManager.PERMISSION_GRANTED == checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


}