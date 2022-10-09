package com.example.allsportstest

import org.json.JSONObject

class ParseJSON {

    companion object {
        fun parseJson(result: String): List<MapObject> {
            val mainObject = JSONObject(result)
            return parseMapObjList(mainObject)

        }

        private fun parseMapObjList(mainObject: JSONObject): List<MapObject> {
            val listMapObject = ArrayList<MapObject>()
            val mapObjArray = mainObject.getJSONArray("results")

            for (i in 0 until mapObjArray.length()) {
                val mapObj = mapObjArray[i] as JSONObject
                val item = MapObject(
                    name = mapObj.getString("name"),
                    lat = mapObj.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                    lng = mapObj.getJSONObject("geometry").getJSONObject("location").getDouble("lng"),
                    rating = if (mapObj.has("rating")){mapObj.getDouble("rating")} else{0.0},
                    address = mapObj.getString("vicinity"),
                    iconUrl = mapObj.getString("icon")
                )
                listMapObject.add(item)
            }
            return listMapObject
        }
    }



}