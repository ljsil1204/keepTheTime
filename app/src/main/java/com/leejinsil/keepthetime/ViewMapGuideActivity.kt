package com.leejinsil.keepthetime

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityViewMapGuideBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
import org.json.JSONObject

class ViewMapGuideActivity : BaseActivity() {

    lateinit var binding : ActivityViewMapGuideBinding

    lateinit var mAppointmentData : AppointmentData

    var path : PathOverlay? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_map_guide)
        mAppointmentData = intent.getSerializableExtra("appointment") as AppointmentData
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        getNaverMapView()

    }

    fun getNaverMapView () {

        binding.naverMapView.getMapAsync {

            val naverMap = it

            val startCoord = LatLng(mAppointmentData.start_latitude, mAppointmentData.start_longitude)
            val finishLatLng = LatLng(mAppointmentData.latitude, mAppointmentData.longitude)

            val cameraUpdate = CameraUpdate.scrollTo(finishLatLng)
            naverMap.moveCamera(cameraUpdate)

            val marker = Marker()
            marker.position = finishLatLng
            marker.map = naverMap


            if (path == null) {
                path = PathOverlay()
            }

            getPubTransPath()

            val coordList = ArrayList<LatLng>()

            coordList.add(startCoord)
            coordList.add(finishLatLng)

            path!!.coords = coordList
            path!!.map = naverMap



        }

    }

    fun getPubTransPath() {

        val ODsayService = ODsayService.init(mContext, "EyorgK6z5ndSx/83u/aKRDPZT3+TFWqHlUOZkc3JR6g")

        val onResultCallbackListener = object : OnResultCallbackListener{

            override fun onSuccess(odsayData: ODsayData?, api: API?) {

                val jsonObj = odsayData!!.json!!
                val resultObj = jsonObj.getJSONObject("result")

                val busCount = resultObj.getInt("busCount")
                val subwayCount = resultObj.getInt("subwayCount")
                val subwayBusCount = resultObj.getInt("subwayBusCount")
                val pointDistance = resultObj.getInt("pointDistance")

                val pathArr = resultObj.getJSONArray("path")
                val pathObjList = ArrayList<JSONObject>()

                for (i in 0 until pathArr.length()) {

                    val pathObj = pathArr.getJSONObject(i)
                    pathObjList.add(pathObj)

                }

                val FirstPathObj = pathObjList[0]
                val subPathArr = FirstPathObj.getJSONArray("subPath")

                val subPathObjList = ArrayList<JSONObject>()

                for (i in 0 until subPathArr.length()) {

                    val subPathObj = subPathArr.getJSONObject(i)
                    subPathObjList.add(subPathObj)
                }

                val stationsObjList = ArrayList<JSONObject>()

                val stationLatLngList = ArrayList<LatLng>()

                for ( spo in subPathObjList ){

                    if (!spo.isNull("passStopList")){

                        val passStopListObj = spo.getJSONObject("passStopList")
                        val stationsArr = passStopListObj.getJSONArray("stations")

                        for (i in 0 until stationsArr.length()) {

                            val stationsObj = stationsArr.getJSONObject(i)
                            stationsObjList.add(stationsObj)

                            val stationLng = stationsObj.getString("x").toDouble()
                            val stationLat = stationsObj.getString("y").toDouble()

                            stationLatLngList.add(LatLng(stationLat, stationLng))

                        }

                    }

                }

                path!!.coords = stationLatLngList

            }

            override fun onError(p0: Int, p1: String?, p2: API?) {

            }

        }

        ODsayService.requestSearchPubTransPath(
            mAppointmentData.start_longitude.toString(),
            mAppointmentData.start_latitude.toString(),
            mAppointmentData.longitude.toString(),
            mAppointmentData.latitude.toString(),
            "0",
            null,
            null,
            onResultCallbackListener
        )

    }

}