package com.leejinsil.keepthetime.datas

import org.json.JSONObject
import java.io.Serializable

class SubPathData : Serializable{

    var trafficType = 0
    var distance = 1.0
    var sectionTime = 0
    var stationCount = 0

    val laneSubwayList = ArrayList<LaneData>()
    val laneBusList = ArrayList<LaneData>()

    var startName = ""
    var startX = 1.0
    var startY = 1.0
    var endName = ""
    var endX = 1.0
    var endY = 1.0

    var way = ""
    var wayCode = 0

    companion object{

        fun getSubPathDataFromJson ( jsonObj : JSONObject) : SubPathData {

            val subPathData = SubPathData()

            subPathData.trafficType = jsonObj.getInt("trafficType")
            subPathData.distance = jsonObj.getDouble("distance")
            subPathData.sectionTime = jsonObj.getInt("sectionTime")
            subPathData.stationCount = jsonObj.getInt("stationCount")

            if (!jsonObj.isNull("lane")){

                val laneArr = jsonObj.getJSONArray("lane")

                for (i in 0 until laneArr.length()) {

                    val laneObj = laneArr.getJSONObject(i)

                    if (subPathData.trafficType == 1) {
                        subPathData.laneSubwayList.add(LaneData.getLaneSubwayDataFromJson(laneObj))
                    }

                    if (subPathData.trafficType == 2) {
                        subPathData.laneBusList.add(LaneData.getLaneBusDataFromJson(laneObj))
                    }

                }

            }

            subPathData.startName = jsonObj.getString("startName")
            subPathData.startX = jsonObj.getDouble("startX")
            subPathData.startY = jsonObj.getDouble("startY")
            subPathData.endName = jsonObj.getString("endName")
            subPathData.endX = jsonObj.getDouble("endX")
            subPathData.endY = jsonObj.getDouble("endY")
            subPathData.way = jsonObj.getString("way")
            subPathData.wayCode = jsonObj.getInt("wayCode")

            return subPathData
        }

    }

}