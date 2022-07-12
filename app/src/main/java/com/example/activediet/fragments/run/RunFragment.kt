package com.example.activediet.fragments.run

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.activediet.data.Run
import com.example.activediet.databinding.FragmentRunBinding
import com.example.activediet.fragments.food.CalculatorFragmentDirections
import com.example.activediet.services.TrackingService
import com.example.activediet.utilities.Constants
import com.example.activediet.utilities.Constants.MAP_ZOOM
import com.example.activediet.utilities.Constants.PAUSE_SERVICE
import com.example.activediet.utilities.Constants.START_OR_RESUME_SERVICE
import com.example.activediet.utilities.Constants.STOP_SERVICE
import com.example.activediet.utilities.run.TrackingUtility
import com.example.activediet.utilities.track
import com.example.activediet.viewmodels.run.RunViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.FragmentComponent
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.round

@AndroidEntryPoint
class RunFragment : Fragment() {

    private var _binding: FragmentRunBinding? = null
    private val binding get() = _binding!!


    private val viewModel: RunViewModel by viewModels()

    private var isTracking = false

    private var pathPoints = mutableListOf<track>()

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    private var map: GoogleMap? = null

    private var curTimeInMs = 0L



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
        requestPermissions()

        // listeners
        setOnClickListeners()

        // subscribe observers
        subscribeToObservers()
    }



    // add tracks

    private fun addLatestTrack(){
        // check path points
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1){

            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()

            // track options
            val trackOptions = PolylineOptions()
                .color(Color.RED)
                .width(8F)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(trackOptions)

        }
    }


    private fun addAllTracks() {
        for(track in pathPoints) {
            val trackOptions = PolylineOptions()
                .color(Color.RED)
                .width(8F)
                .addAll(track)
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
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }



    // zoom track
    private fun zoomTrack() : Int {
        val bounce = LatLngBounds.builder()
        var counter = 0
        for (track in pathPoints){
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
                    (binding.mapView.height * 0.05).toInt()
                )
            )
        return counter
    }



    // subscribeToObservers

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner) {
            updateTracking(it)
        }
        TrackingService.pathPoints.observe(viewLifecycleOwner) {
            pathPoints = it
            addLatestTrack()
            moveCameraToUser()
        }
        TrackingService.timeRunInMs.observe(viewLifecycleOwner) {
            curTimeInMs = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(curTimeInMs, true)
            binding.timer!!.text = formattedTime
        }
    }


    // save db

    private fun saveRunInDB(){
        map?.snapshot { bitmap ->
            var distInMeters = 0
            for (track in pathPoints){
                distInMeters += viewModel.calcLength(track).toInt()
            }
            val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())

            val time = curTimeInMs
            // values
            val date = dateFormat.format(Calendar.getInstance().time)
            val avgSpeed = round((distInMeters / 1000f) / (curTimeInMs / 1000f / 60 / 60) * 10) / 10f
            val weight = sharedPrefs.getFloat(Constants.KEY_WEIGHT, 0f).toString().toFloat()
            val calsBurned = ((distInMeters/1000.toFloat())* weight).toInt()

            // run
            val run = Run(bitmap,date, avgSpeed, distInMeters, curTimeInMs, calsBurned)
            viewModel.insertRun(run)

            Toast.makeText(context,"Run Save successfully", Toast.LENGTH_SHORT).show()
        }
    }


    // init map
    private fun initMap(savedInstanceState: Bundle?){
        binding.apply {
            mapView.apply {
                onCreate(savedInstanceState)
                getMapAsync {
                    map = it
                    addAllTracks()
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
                zoomTrack()
                // check if not empty run
                if (pathPoints.isNotEmpty()) {
                    saveRunInDB()
                    stopRun()
                }

            }
        }
    }


    // send commands

    private fun stopRun(){
        sendCommandToService(STOP_SERVICE)
    }


    private fun sendCommandToService(action: String) =
        Intent(context, TrackingService::class.java).also {
            it.action = action
            context?.startService(it)
        }



    // permissions - buggy need to fix

    private fun requestPermissions(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            binding.apply {
                mapView.apply {
                    getMapAsync {
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                        }
                        it.isMyLocationEnabled = true
                        it.uiSettings.isMyLocationButtonEnabled = true
                        it.uiSettings.isZoomControlsEnabled = true
                    }
                }
            }
            // map updated
            return
        }
        else{
             EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION

            )
//            val action = RunFragmentDirections.actionTrackingFragmentToWelcomeFragment()
//            findNavController().navigate(action)

        }
    }
}