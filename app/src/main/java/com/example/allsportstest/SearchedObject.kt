package com.example.allsportstest

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class SearchedObject(
    val name: String,
    val lat: Double,
    val lng: Double,
//    val rating: Double?,
    val address: String,
    val iconUrl: String
) : ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(lat, lng)
    }

    override fun getTitle(): String = name

    override fun getSnippet(): String = address

}
