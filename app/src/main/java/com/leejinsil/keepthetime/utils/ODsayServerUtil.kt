package com.leejinsil.keepthetime.utils

import android.content.Context
import android.util.Log
import com.leejinsil.keepthetime.datas.AppointmentData
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
import org.json.JSONObject

class ODsayServerUtil {

    interface JsonResponseHandler {
        fun onResponse( jsonObj : JSONObject )
    }

    companion object {

        private val APIKey = "EyorgK6z5ndSx/83u/aKRDPZT3+TFWqHlUOZkc3JR6g"

        fun getRequestSearchPubTransPath(context: Context, data : AppointmentData, handlder : JsonResponseHandler?) {

            val ODsayService = ODsayService.init(context, APIKey)

            val onResultCallbackListener = object : OnResultCallbackListener {

                override fun onSuccess(odsayData: ODsayData?, api: API?) {

                    val jsonObj = odsayData!!.json!!
                    val resultObj = jsonObj.getJSONObject("result")

                    Log.d("서버 응답", resultObj.toString())

                    handlder?.onResponse(resultObj)

                }

                override fun onError(p0: Int, p1: String?, p2: API?) {

                }

            }

            ODsayService.requestSearchPubTransPath(
                data.start_longitude.toString(),
                data.start_latitude.toString(),
                data.longitude.toString(),
                data.latitude.toString(),
                "0",
                null,
                null,
                onResultCallbackListener
            )

        }

    }

}