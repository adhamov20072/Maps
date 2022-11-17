package com.aimardon.maps

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.aimardon.maps.databinding.ActivityMapsBinding
import com.google.android.gms.common.internal.zzx
import com.google.android.gms.common.util.MapUtils
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.*
import kotlin.math.abs

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermissionLauncher = registerForActivityResult(RequestPermission()) {
            if (it) {
                mMap.isMyLocationEnabled = true
            }
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.apply {
            isRotateGesturesEnabled=true
            isMyLocationButtonEnabled=true
            isZoomGesturesEnabled=true
            isZoomControlsEnabled=true
        }
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        val given = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (given) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        val mashhur = arrayListOf<LatLng>(
            LatLng(40.3760030877985, 71.75196938126479)
        )
        for (i in 0 until mashhur.size) {
            mMap.addMarker(MarkerOptions().position(mashhur[i]))

            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        abs(mashhur[i].latitude),
                        abs(mashhur[i].longitude)
                    ), 10f
                ), 2000, null)
        }
    }
}