package com.example.activediet.fragments.run

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.activediet.R
import com.example.activediet.data.Run
import com.example.activediet.databinding.FragmentTrackingBinding
import com.example.activediet.services.TrackingService
import com.example.activediet.utilities.Constants
import com.example.activediet.utilities.Constants.ACTION_PAUSE_SERVICE
import com.example.activediet.utilities.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.activediet.utilities.Constants.ACTION_STOP_SERVICE
import com.example.activediet.utilities.Constants.MAP_ZOOM
import com.example.activediet.utilities.run.TrackingUtility
import com.example.activediet.utilities.track
import com.example.activediet.viewmodels.run.RunViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import javax.inject.Inject
import kotlin.math.round


class TrackingFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!


    private val viewModel: RunViewModel by viewModels()

    private var isTracking = false

    private var pathPoints = mutableListOf<track>()

    private var map: GoogleMap? = null

    private var curTimeInMs = 0L

    private var menu: Menu? = null

    @set:Inject
    var weight = 80f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        requestPermissions()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // listeners
        setOnClickListeners()
        // map
        initMap(savedInstanceState)

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
            menu?.getItem(0)?.isVisible = true
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
    private fun zoomTrack(){
        val bounce = LatLngBounds.builder()
        for (track in pathPoints){
            for (pos in track){
                bounce.include(pos)
            }
        }

        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounce.build(),
                binding.mapView.width,
                binding.mapView.height,
                (binding.mapView.height * 0.05).toInt()
            )
        )
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

    private fun SaveRunInDB(){
        map?.snapshot { bitmap ->
            var distInMeters = 0
            for (track in pathPoints){
                distInMeters += TrackingUtility.calcTrackLength(track).toInt()
            }
            val avgSpeed = round((distInMeters / 1000f) / (curTimeInMs / 1000f / 60 / 60) * 10) / 10f
            val dateTimeStamp = Calendar.getInstance().timeInMillis
            val calsBurned = ((distInMeters/1000)* weight).toInt()

            val run = Run(bitmap, dateTimeStamp, avgSpeed, distInMeters, curTimeInMs, calsBurned)
            viewModel.insertRun(run)

            Toast.makeText(context,"Run Save successfully", Toast.LENGTH_SHORT).show()
            stopRun()
        }
    }


    // init map
    private fun initMap(savedInstanceState: Bundle?){
        binding.apply {
            mapView.apply {
                onCreate(savedInstanceState)
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
                toggleRun()
            }
            btnFinishRun.setOnClickListener {
                zoomTrack()
                SaveRunInDB()
            }
        }
    }


    // send commands


    private fun toggleRun() {
        if(isTracking) {
            menu?.getItem(0)?.isVisible = true
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }


    private fun stopRun(){
        sendCommandToService(ACTION_STOP_SERVICE)
    }


    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    // dialog cancel - maybe unnecessary

    private fun showCancelDialog(){
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cancel the Run?")
            .setMessage("Are you sure to cancel the current run and delete all the data")
            .setIcon(R.drawable.remove_circle)
            .setPositiveButton("Yes"){ _,_ ->
                stopRun()
            }
            .setNegativeButton("No"){dialog, _ ->
                dialog.cancel()
            }
            .create()

        dialog.show()
    }





    // permissions - buggy need to fix

    private fun requestPermissions(){
        if (TrackingUtility.hasLocationPermissions(requireContext())){
            return
        }
        else{
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permissions to use this app.",
                    Constants.REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
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
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) { }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()
        }
        else{
            requestPermissions()
        }
    }

    // Deprecated code
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}