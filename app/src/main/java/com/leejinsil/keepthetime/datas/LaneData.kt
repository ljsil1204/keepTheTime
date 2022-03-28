package com.leejinsil.keepthetime.datas

import org.json.JSONObject
import java.io.Serializable

class LaneData : Serializable {

    var busNo = ""
    var name = ""
    var subwayCode = 0

    companion object {

        fun getLaneBusDataFromJson ( jsonObj : JSONObject) : LaneData {

            val laneData = LaneData()

            laneData.busNo = jsonObj.getString("busNo")

            return laneData

        }

        fun getLaneSubwayDataFromJson ( jsonObj : JSONObject) : LaneData {

            val laneData = LaneData()

            laneData.name = jsonObj.getString("name")
            laneData.subwayCode = jsonObj.getInt("subwayCode")

            return laneData

        }

    }

}