package com.example.allsportstest


import android.graphics.Point
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.allsportstest.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import com.google.maps.android.clustering.ClusterManager
import kotlinx.coroutines.launch


class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback {

    companion object {
        private const val START_LAT = 53.9
        private const val START_LNG = 27.5667
        private const val DEFAULT_ZOOM = 14f
        private const val DEFAULT_RADIUS_SEARCH = 1500.0
    }

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            mainViewModel.requestObject(
                this@MapsActivity,
                START_LAT,
                START_LNG,
                DEFAULT_RADIUS_SEARCH
            )
        }

        mainViewModel.liveDataError.observe(this) {
            if (it != null) {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))
        val cameraPosition = CameraPosition.builder()
            .target(LatLng(START_LAT, START_LNG))
            .zoom(DEFAULT_ZOOM)
            .build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        map.animateCamera(cameraUpdate)

        mainViewModel.liveDataListOfMapObj.observe(this) { list ->
            addMarker(googleMap, list)
        }
    }

    private fun addMarker(
        googleMap: GoogleMap,
        markerList: List<MapObject>
    ) {
        markerList.forEach {
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(it.name)
                    .position(LatLng(it.lat, it.lng))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker))
            )
            marker?.tag =it
        }

        googleMap.setOnCameraIdleListener {
            lifecycleScope.launch {
                val center = googleMap.cameraPosition.target
                val fromCenterToTarget = searchRadius(center, googleMap)
                mainViewModel.requestObject(
                    this@MapsActivity,
                    center.latitude,
                    center.longitude,
                    fromCenterToTarget
                )
            }
        }
    }
}