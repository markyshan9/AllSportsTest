package com.example.allsportstest


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.allsportstest.BuildConfig.MAPS_API_KEY
import org.json.JSONObject

class MainViewModel : ViewModel() {


    private val _liveDataList = MutableLiveData<List<SearchedObject>>()
    var liveDataList: LiveData<List<SearchedObject>> = _liveDataList


    fun requestObject(context: Context, lat: Double, lng: Double) {
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=${lat},${lng}&" +
                "radius=2000&" +
                "types=casino&" +
                "sensor=false&" +
                "key=${MAPS_API_KEY}"

        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                parseJson(result)
            },
            { error ->
                Toast.makeText(context, "Error volley", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(request)
    }

    private fun parseJson(result: String) {
        val mainObject = JSONObject(result)
        val listCafe = parseCafeList(mainObject)
        _liveDataList.value = listCafe

    }


    private fun parseCafeList(mainObject: JSONObject): List<SearchedObject> {
        val listSearchedObject = ArrayList<SearchedObject>()
        val cafeArray = mainObject.getJSONArray("results")

        for (i in 0 until cafeArray.length()) {
            val cafe = cafeArray[i] as JSONObject
            val item = SearchedObject(
                name = cafe.getString("name"),
                lat = cafe.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                lng = cafe.getJSONObject("geometry").getJSONObject("location").getDouble("lng"),
//                rating = cafe.getDouble("rating"),
                address = cafe.getString("vicinity"),
                iconUrl = cafe.getString("icon")
            )
            listSearchedObject.add(item)
        }
        return listSearchedObject
    }


}