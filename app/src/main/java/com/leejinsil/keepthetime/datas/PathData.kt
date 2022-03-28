package com.leejinsil.keepthetime.datas

import org.json.JSONObject
import java.io.Serializable

class PathData : Serializable{

    var pathType = 0
    var info = InfoData()
    val subPathList = ArrayList<SubPathData>()

    companion object {

        fun getPathDataFromJson ( jsonObj : JSONObject ) : PathData {

            val pathData = PathData()

            pathData.pathType = jsonObj.getInt("pathType")

            pathData.info = InfoData.getInfoDataFromJson(jsonObj.getJSONObject("info"))

            val subPathArr = jsonObj.getJSONArray("subPath")

            for (i in 0 until subPathArr.length()){
                val subPathObj = subPathArr.getJSONObject(i)
                pathData.subPathList.add(SubPathData.getSubPathDataFromJson(subPathObj))
            }

            return pathData

        }

    }

}