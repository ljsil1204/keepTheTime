package com.leejinsil.keepthetime.datas

import org.json.JSONObject

class SubPathData{

    var trafficType = 0
    var distance = 0
    var sectionTime = 0
    var stationCount = 0

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
            subPathData.distance = jsonObj.getInt("distance")
            subPathData.sectionTime = jsonObj.getInt("sectionTime")
            subPathData.stationCount = jsonObj.getInt("stationCount")
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