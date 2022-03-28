package com.leejinsil.keepthetime.datas

import org.json.JSONObject
import java.io.Serializable

class StationsData : Serializable{

    var index = 0
    var stationID = 0
    var stationName = ""
    var x = ""
    var y = ""

    companion object{

        fun getStationsDataFromJson (jsonObj : JSONObject) : StationsData {

            var stationsData = StationsData()

            stationsData.index = jsonObj.getInt("index")
            stationsData.stationID = jsonObj.getInt("stationID")
            stationsData.stationName = jsonObj.getString("stationName")
            stationsData.x = jsonObj.getString("x")
            stationsData.y = jsonObj.getString("y")

            return stationsData

        }

    }

}