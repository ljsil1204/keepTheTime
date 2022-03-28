package com.leejinsil.keepthetime.datas

import org.json.JSONObject

class ResultData {

    var busCount = 0
    var subwayCount = 0
    var subwayBusCount = 0

    val pathList = ArrayList<PathData>()

    companion object {

        fun getResultDataFromJson ( jsonObj : JSONObject) : ResultData {

            val resultData = ResultData()

            resultData.busCount = jsonObj.getInt("busCount")
            resultData.subwayCount = jsonObj.getInt("subwayCount")
            resultData.subwayBusCount = jsonObj.getInt("subwayBusCount")

            val pathArr = jsonObj.getJSONArray("path")

            for (i in 0 until pathArr.length()){
                val pathObj = pathArr.getJSONObject(i)
                resultData.pathList.add(PathData.getPathDataFromJson(pathObj))
            }

            return resultData

        }

    }

}