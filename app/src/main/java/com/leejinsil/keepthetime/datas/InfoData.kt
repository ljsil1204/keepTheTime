package com.leejinsil.keepthetime.datas

import org.json.JSONObject

class InfoData {

    var trafficDistance = 0
    var totalWalk = 0
    var totalTime = 0
    var payment = 0
    var busTransitCount = 0
    var subwayTransitCount = 0
    var firstStartStation = ""
    var lastEndStation = ""
    var totalStationCount = 0
    var busStationCount = 0
    var subwayStationCount = 0
    var totalDistance = 0

    companion object {

        fun getInfoDataFromJson ( jsonObj : JSONObject) : InfoData {

            val infoData = InfoData()

            infoData.trafficDistance = jsonObj.getInt("trafficDistance")
            infoData.totalWalk = jsonObj.getInt("totalWalk")
            infoData.totalTime = jsonObj.getInt("totalTime")
            infoData.payment = jsonObj.getInt("payment")
            infoData.busTransitCount = jsonObj.getInt("busTransitCount")
            infoData.subwayTransitCount = jsonObj.getInt("subwayTransitCount")
            infoData.firstStartStation = jsonObj.getString("subwayTransitCount")
            infoData.lastEndStation = jsonObj.getString("lastEndStation")
            infoData.totalStationCount = jsonObj.getInt("totalStationCount")
            infoData.busStationCount = jsonObj.getInt("busStationCount")
            infoData.subwayStationCount = jsonObj.getInt("subwayStationCount")
            infoData.totalDistance = jsonObj.getInt("totalDistance")

            return infoData
        }

    }

}