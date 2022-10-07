package com.example.allsportstest

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class ObjectRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<SearchedObject>
) : DefaultClusterRenderer<SearchedObject>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: SearchedObject, markerOptions: MarkerOptions) {
        markerOptions.title(item.name)
            .position(LatLng(item.lat, item.lng))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker))

    }

    override fun onClusterItemRendered(clusterItem: SearchedObject, marker: Marker) {
        marker.tag = clusterItem
    }
}