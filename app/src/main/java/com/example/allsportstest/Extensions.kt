package com.example.allsportstest

import android.app.Activity
import android.graphics.Point
import android.util.DisplayMetrics
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

fun Activity.searchRadius(center: LatLng, map: GoogleMap): Double {
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)

    val targetPoint = Point(
        metrics.widthPixels/2,
        metrics.heightPixels - metrics.heightPixels / 9
    )
    val targetLatLng = map.projection.fromScreenLocation(targetPoint)
    return SphericalUtil.computeDistanceBetween(
        center,
        targetLatLng
    )
}