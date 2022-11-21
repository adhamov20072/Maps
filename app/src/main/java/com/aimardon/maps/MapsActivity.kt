package com.aimardon.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.aimardon.maps.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.*
import kotlin.math.abs

class MapsActivity : AppCompatActivity(), OnMapReadyCallback{
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    val markerOptions = MarkerOptions()
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        requestPermissionLauncher = registerForActivityResult(RequestPermission()) {
            if (it) {
                mMap.isMyLocationEnabled = true
            }
        }
    }
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val given =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        if (given) {
            mMap.isMyLocationEnabled = true
        }else{
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION,null)
        }
        mMap.uiSettings.apply {
            isRotateGesturesEnabled = true
            isMyLocationButtonEnabled = true
            isZoomGesturesEnabled = true
            isZoomControlsEnabled = true
        }
        val mashhur = arrayListOf<LatLng>(
            LatLng(40.3760030877985, 71.75196938126479)
        )
        mashhur.forEach { LatLng ->
            val markerlocation =
                Geocoder(this)
                    .getFromLocation(LatLng.latitude, LatLng.longitude, 1)[0]
                    .featureName
            if (markerlocation != null) {
                Log.d("keylari", markerlocation.toString())
                mMap.addMarker(markerOptions.position(LatLng).title(markerlocation.toString()))
            }
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        abs(LatLng.latitude),
                        abs(LatLng.longitude)
                    ), 10f
                ), 2000, null
            )
        }
    }
}