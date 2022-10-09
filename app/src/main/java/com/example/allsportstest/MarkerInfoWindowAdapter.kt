package com.example.allsportstest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class MarkerInfoWindowAdapter(
    private val context: Context
) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View? {
        val srchObj = marker.tag as? MapObject ?: return null

        val view = LayoutInflater.from(context).inflate(
            R.layout.marker_info_contents, null
        )
        view.findViewById<TextView>(
            R.id.text_view_title
        ).text = srchObj.name
        view.findViewById<TextView>(
            R.id.text_view_address
        ).text = srchObj.address
        view.findViewById<TextView>(
            R.id.text_view_rating
        ).text = "Rating: %.2f".format(srchObj.rating)

        return view
    }

    override fun getInfoWindow(p0: Marker): View? {
        return null
    }
}