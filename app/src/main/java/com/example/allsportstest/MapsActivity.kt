package com.example.allsportstest


import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.allsportstest.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import kotlinx.coroutines.launch

private const val START_LAT = 53.9
private const val START_LNG = 27.5667

class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            mainViewModel.requestObject(this@MapsActivity, START_LAT, START_LNG)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        addMarker(mMap)
        val cameraPosition = CameraPosition.builder()
            .target(LatLng(START_LAT, START_LNG))
            .zoom(14f)
            .build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        mMap.animateCamera(cameraUpdate)


        /**
        without Customize markers
         */
//        mMap.setOnCameraIdleListener {
//            lifecycleScope.launch {
//                val center = mMap.cameraPosition.target
//                Log.d("log", center.toString())
//                mainViewModel.requestObject(this@MapsActivity, center.latitude, center.longitude)
//            }
//        }

        googleMap.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))


    }

    private fun addMarker(googleMap: GoogleMap) {
        mainViewModel.liveDataList.observe(this) { list ->
            val clusterManager = ClusterManager<SearchedObject>(this, googleMap)
            clusterManager.renderer =
                ObjectRenderer(
                    this,
                    googleMap,
                    clusterManager
                )
            clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))
            clusterManager.addItems(list)
            clusterManager.cluster()
            googleMap.setOnCameraIdleListener {
                lifecycleScope.launch {
                    val center = googleMap.cameraPosition.target
                    Log.d("log", center.toString())
                    mainViewModel.requestObject(
                        this@MapsActivity,
                        center.latitude,
                        center.longitude
                    )
                }
                clusterManager.onCameraIdle()
            }

            /**
            without Customize markers
             */
//            for (i in list.indices) {
//                val cafe = list[i]
//                val marker = LatLng(cafe.lat, cafe.lng)
//                val addMarker = googleMap.addMarker(
//                    MarkerOptions()
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker))
//                        .position(marker)
//                        .title(cafe.name)
//                )
//
//                    addMarker!!.tag = cafe
//
//            }
        }
    }
}