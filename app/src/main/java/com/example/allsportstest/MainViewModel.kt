package com.example.allsportstest


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.allsportstest.BuildConfig.MAPS_API_KEY

class MainViewModel : ViewModel() {


    private val _liveDataListOfMapObj = MutableLiveData<List<MapObject>>()
    var liveDataListOfMapObj: LiveData<List<MapObject>> = _liveDataListOfMapObj

    private val _liveDataError = MutableLiveData<String>(null)
    var liveDataError: LiveData<String> = _liveDataError


    fun requestObject(context: Context, lat: Double, lng: Double, radius: Double) {
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=${lat},${lng}&" +
                "radius=${radius}&" +
                "types=casino&" +
                "sensor=false&" +
                "key=${MAPS_API_KEY}&" +
                "language=ru"

        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                _liveDataListOfMapObj.value = ParseJSON.parseJson(result)
            },
            { error ->Log.e("error", context.getString(R.string.error_network))
                _liveDataError.value = context.getString(R.string.error_network)
            }
        )
        queue.add(request)
    }




}