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

    var PassStopList = ArrayList<StationsData>()

    companion object{

        fun getSubPathDataFromJson ( jsonObj : JSONObject) : SubPathData {

            val subPathData = SubPathData()

            subPathData.trafficType = jsonObj.getInt("trafficType")
            subPathData.distance = jsonObj.getDouble("distance")
            subPathData.sectionTime = jsonObj.getInt("sectionTime")

            if (!jsonObj.isNull("stationCount")){
                subPathData.stationCount = jsonObj.getInt("stationCount")
            }

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

            if (!jsonObj.isNull("startName")){
                subPathData.startName = jsonObj.getString("startName")
            }

            if (!jsonObj.isNull("startX")){
                subPathData.startX = jsonObj.getDouble("startX")
            }

            if (!jsonObj.isNull("startY")){
                subPathData.startY = jsonObj.getDouble("startY")
            }

            if (!jsonObj.isNull("endName")){
                subPathData.endName = jsonObj.getString("endName")
            }

            if (!jsonObj.isNull("endX")){
                subPathData.endX = jsonObj.getDouble("endX")
            }

            if (!jsonObj.isNull("endY")){
                subPathData.endY = jsonObj.getDouble("endY")
            }

            if (!jsonObj.isNull("way")){
                subPathData.way = jsonObj.getString("way")
            }

            if (!jsonObj.isNull("wayCode")){
                subPathData.wayCode = jsonObj.getInt("wayCode")
            }

            if (!jsonObj.isNull("passStopList")){

                val passStopListObj = jsonObj.getJSONObject("passStopList")

                val stationsArr = passStopListObj.getJSONArray("stations")

                for (i in 0 until stationsArr.length()) {

                    val stationsObj = stationsArr.getJSONObject(i)
                    subPathData.PassStopList.add(StationsData.getStationsDataFromJson(stationsObj))

                }

            }

            return subPathData
        }

    }

}