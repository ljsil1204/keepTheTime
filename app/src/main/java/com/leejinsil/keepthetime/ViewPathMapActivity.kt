package com.leejinsil.keepthetime

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityViewMapGuideBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.PathData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.MarkerIcons

class ViewPathMapActivity : BaseActivity() {

    lateinit var binding : ActivityViewMapGuideBinding

    lateinit var mAppointmentData : AppointmentData
    lateinit var mPathData : PathData

    lateinit var naverMap : NaverMap

    var path : PathOverlay? = null

    val stationLatLngList = ArrayList<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_map_guide)
        mAppointmentData = intent.getSerializableExtra("appointment") as AppointmentData
        mPathData = intent.getSerializableExtra("path") as PathData
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        actionBarTitle.setText(R.string.actionbar_title_detail_path)

        getNaverMapView()

    }

    fun getNaverMapView () {

        binding.naverMapView.getMapAsync {

            naverMap = it

            val startLatLng = LatLng(mAppointmentData.start_latitude, mAppointmentData.start_longitude)
            val finishLatLng = LatLng(mAppointmentData.latitude, mAppointmentData.longitude)

            val coordList = ArrayList<LatLng>()
            coordList.add(startLatLng)
            coordList.add(finishLatLng)

            val cameraUpdate = CameraUpdate.scrollTo(finishLatLng)
            naverMap.moveCamera(cameraUpdate)

            val markerStart = Marker()
            markerStart.position = startLatLng
            markerStart.map = naverMap
            markerStart.icon = MarkerIcons.BLACK
            markerStart.iconTintColor = ContextCompat.getColor(mContext, R.color.basic_900)

            val markerFinish = Marker()
            markerFinish.position = finishLatLng
            markerFinish.map = naverMap
            markerFinish.icon = MarkerIcons.BLACK
            markerFinish.iconTintColor = ContextCompat.getColor(mContext, R.color.primary)

            getPubTransPath()

        }

    }

    fun getPubTransPath() {

        if (path == null) {
            path = PathOverlay()
        }

        stationLatLngList.add(LatLng(mAppointmentData.start_latitude, mAppointmentData.start_longitude)) // ?????? ??????

        when(mPathData.pathType) {

            1 -> {

                for (subPathObj in mPathData.subPathList) {

                    if (subPathObj.trafficType == 1) {

                        for (station in subPathObj.PassStopList) {

                            stationLatLngList.add(LatLng(station.y.toDouble(), station.x.toDouble() ))

                        }

                    }

                }

            }

            2 -> {

                for (subPathObj in mPathData.subPathList) {

                    if (subPathObj.trafficType == 2) {

                        for (station in subPathObj.PassStopList) {

                            stationLatLngList.add(LatLng(station.y.toDouble(), station.x.toDouble() ))

                        }

                    }

                }

            }

            3 -> {

                for (subPathObj in mPathData.subPathList) {

                    if (subPathObj.trafficType == 1 || subPathObj.trafficType == 2) {

                        for (station in subPathObj.PassStopList) {

                            stationLatLngList.add(LatLng(station.y.toDouble(), station.x.toDouble() ))

                        }

                    }

                }

            }

        }

        stationLatLngList.add(LatLng(mAppointmentData.latitude, mAppointmentData.longitude)) // ?????? ??????

        path!!.coords = stationLatLngList

        path!!.color = ContextCompat.getColor(mContext, R.color.primary)

        path!!.map = naverMap

    }


}