package com.leejinsil.keepthetime

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityViewAppointmentDetailBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.fragments.SubwayFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class ViewAppointmentDetailActivity : BaseActivity() {

    lateinit var binding : ActivityViewAppointmentDetailBinding

    lateinit var mAppointmentData : AppointmentData

    var marker : Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_appointment_detail)
        mAppointmentData = intent.getSerializableExtra("appointment") as AppointmentData
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnMap.setOnClickListener {

            val myIntent = Intent(mContext, ViewMapDetailActivity::class.java)
            myIntent.putExtra("appointment", mAppointmentData)
            startActivity(myIntent)

        }

        binding.btnEdit.setOnClickListener {

            val myIntent = Intent(mContext, EditAppointmentActivity::class.java)
            myIntent.putExtra("appointment", mAppointmentData)
            startActivity(myIntent)

        }

        binding.btnDelete.setOnClickListener {

            deleteAppointmentFromServer()

        }

    }

    override fun setValues() {

        setUi()

    }

    fun setUi() {
        binding.txtTitle.text = mAppointmentData.title
        binding.txtDate.text = SimpleDateFormat("yy/MM/dd (E)").format(mAppointmentData.datetime.time)
        binding.txtHour.text = SimpleDateFormat("a h:mm").format(mAppointmentData.datetime.time)
        binding.txtStartPlace.text = mAppointmentData.start_place
        binding.txtFinishPlace.text = mAppointmentData.place

        getNaverMapView()
    }

    fun getNaverMapView () {

        binding.naverMapView.getMapAsync {

            val naverMap = it

            val selectedLatLng = LatLng(mAppointmentData.latitude, mAppointmentData.longitude)

            val cameraUpdate = CameraUpdate.scrollTo(selectedLatLng)
            naverMap.moveCamera(cameraUpdate)

            if (marker != null) {
                marker!!.map = null
            }

            marker = Marker()
            marker!!.position = selectedLatLng
            marker!!.map = naverMap

        }

    }

    fun getAppointmentDetailFormServer() {

        apiList.getRequestMyAppointmentDetail(mAppointmentData.id.toString()).enqueue( object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful) {

                    val br = response.body()!!

                    mAppointmentData = br.data.appointment

                    setUi()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

    }

    fun deleteAppointmentFromServer() {

        apiList.deleteRequestAppointment(mAppointmentData.id).enqueue( object : Callback<BasicResponse>{

            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    Toast.makeText(mContext, "약속이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                    
                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

    }

    override fun onResume() {
        super.onResume()
        getAppointmentDetailFormServer()

    }

}