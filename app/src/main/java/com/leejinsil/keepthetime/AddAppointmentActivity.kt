package com.leejinsil.keepthetime

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityAddAppointmentBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AddAppointmentActivity : BaseActivity() {

    lateinit var binding : ActivityAddAppointmentBinding

    val mSelectedAppointmentTime = Calendar.getInstance()

    var marker : Marker? = null

    var mSelectedLatLng : LatLng? = null

    var mStartSelectedLatLng : LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_appointment)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        날짜 선택 -> DatePickerDialog
        binding.btnDay.setOnClickListener {

            val dsl = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    mSelectedAppointmentTime.set(year, month, dayOfMonth)
                    getTodayFormat()

                }
            }

            val dpd = DatePickerDialog(
                mContext,
                dsl,
                mSelectedAppointmentTime.get(Calendar.YEAR),
                mSelectedAppointmentTime.get(Calendar.MONTH),
                mSelectedAppointmentTime.get(Calendar.DAY_OF_MONTH)
            ).show()

        }

//        시간 선택 -> TimePickDialog
        binding.btnHour.setOnClickListener {

            val tsl = object : TimePickerDialog.OnTimeSetListener{

                override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {

                    mSelectedAppointmentTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    mSelectedAppointmentTime.set(Calendar.MINUTE,minute)
                    getNowHourFormat()
                }

            }

            val tpd = TimePickerDialog(
                mContext,
                tsl,
                mSelectedAppointmentTime.get(Calendar.HOUR_OF_DAY),
                mSelectedAppointmentTime.get(Calendar.MINUTE),
                false
            ).show()

        }

//        약속 등록 -> api
        binding.btnAppointmentSave.setOnClickListener {
            postMyAppointmentToServer()
        }

    }

    override fun setValues() {

        getTodayFormat()
        getNowHourFormat()
        getNaverMapView()
        getStartNaverMapView()

    }

    fun getTodayFormat () {

//        오늘 날짜가 지정한 형식으로 나오도록
        val sdf = SimpleDateFormat("yy/MM/dd (E)")
        binding.btnDay.text = sdf.format(mSelectedAppointmentTime.time)
    }

    fun getNowHourFormat () {

//        현재 시간이 지정한 형식으로 나오도록
        val sdf = SimpleDateFormat("a h:mm")
        binding.btnHour.text = sdf.format(mSelectedAppointmentTime.time)
    }

    fun getNaverMapView () {

        binding.naverMapView.getMapAsync {

            val naverMap = it

            val coord = LatLng(37.5670135, 126.9783740) // 임시 좌표

            naverMap.setOnMapClickListener { pointF, latLng ->

                if (marker == null) {
                    marker = Marker()
                }

                marker!!.position = latLng
                marker!!.map = naverMap

                mSelectedLatLng = latLng

            }


        }

    }

    fun getStartNaverMapView () {

        binding.naverMapViewStart.getMapAsync {

            val naverMap = it

            naverMap.setOnMapClickListener { pointF, latLng ->

                if (marker == null) {
                    marker = Marker()
                }

                marker!!.position = latLng
                marker!!.map = naverMap

                mStartSelectedLatLng = latLng

            }


        }

    }

    fun postMyAppointmentToServer() {

        val inputTitle = binding.edtTitle.text.toString()

//        제목 입력 안 할 시 종료
        if (inputTitle.isEmpty()){
            Toast.makeText(mContext, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val sdfServer = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val inputDateTime = sdfServer.format(mSelectedAppointmentTime.time)

//        현재 날짜보다 이전 날짜일 경우 종료
        val today = Calendar.getInstance()
        val sdfDay = SimpleDateFormat("yyyy MM dd")

        if (sdfDay.format(mSelectedAppointmentTime.time).compareTo(sdfDay.format(today.time)) < 0 ){
            Toast.makeText(mContext, "현재 이후의 날짜로 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val inputPlace = binding.edtPlace.text.toString()

//        도착 장소 입력 안 할 시 종료
        if (inputPlace.isEmpty()){
            Toast.makeText(mContext, "도착 장소를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }


        val inputStartPlace = binding.edtStartPlace.text.toString()

//        도착 장소 입력 안 할 시 종료
        if (inputStartPlace.isEmpty()){
            Toast.makeText(mContext, "출발 장소를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

//        네이버 지도 도착 위치 클릭 안할 시 - 출발장소
        if (mStartSelectedLatLng == null) {
            Toast.makeText(mContext, "지도를 클릭하여 출발 위치를 설정해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val inputStartLat = mStartSelectedLatLng!!.latitude
        val inputStartLng = mStartSelectedLatLng!!.longitude

//        네이버 지도 도착 위치 클릭 안할 시 - 도착장소
        if (mSelectedLatLng == null) {
            Toast.makeText(mContext, "지도를 클릭하여 도착 위치를 설정해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val inputLat = mSelectedLatLng!!.latitude
        val inputLng = mSelectedLatLng!!.longitude


        apiList.postRequestAddAppointment(
            inputTitle,
            inputDateTime,
            inputStartPlace,
            inputStartLat,
            inputStartLng,
            inputPlace,
            inputLat,
            inputLng
        ).enqueue( object : Callback<BasicResponse>{
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {

                if (response.isSuccessful){
                    Toast.makeText(mContext, "약속 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

    }

}