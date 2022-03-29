package com.leejinsil.keepthetime

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityEditAppointmentBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.BasicResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EditAppointmentActivity : BaseActivity() {

    lateinit var binding : ActivityEditAppointmentBinding

    lateinit var mAppointmentData : AppointmentData

    val mSelectedAppointmentTime = Calendar.getInstance()

    lateinit var naverMap : NaverMap

    lateinit var mSelectedLatLng : LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        mAppointmentData = intent.getSerializableExtra("appointment") as AppointmentData
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        선택한 날짜를 받아온 데이터로 대입
        mSelectedAppointmentTime.time = mAppointmentData.datetime

//        날짜 선택 -> DatePickerDialog
        binding.btnDay.setOnClickListener {

            val dsl = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    mSelectedAppointmentTime.set(year, month, dayOfMonth)
                    val sdfDay = SimpleDateFormat("yy/MM/dd (E)")
                    binding.btnDay.text = sdfDay.format(mSelectedAppointmentTime.time)

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
                        val sdfHour = SimpleDateFormat("a h:mm")
                        binding.btnHour.text = sdfHour.format(mSelectedAppointmentTime.time)
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


        binding.btnAppointmentSave.setOnClickListener {

            putMyEditAppointmentToServer()

        }

    }

    override fun setValues() {

        binding.edtTitle.setText(mAppointmentData.title)
        getDayFormat()
        getHourFormat()
        binding.edtPlace.setText(mAppointmentData.place)

        getNaverMapView()

    }

    fun getDayFormat () {

        val sdfDay = SimpleDateFormat("yy/MM/dd (E)")
        binding.btnDay.text = sdfDay.format(mAppointmentData.datetime.time)

    }

    fun getHourFormat () {

        val sdfHour = SimpleDateFormat("a h:mm")
        binding.btnHour.text = sdfHour.format(mAppointmentData.datetime.time)

    }

    fun getNaverMapView () {

        binding.naverMapView.getMapAsync {

            naverMap = it

            mSelectedLatLng = LatLng(mAppointmentData.latitude, mAppointmentData.longitude)

            val cameraUpdate = CameraUpdate.scrollTo(mSelectedLatLng)
            naverMap.moveCamera(cameraUpdate)

            val marker = Marker()
            marker.position = mSelectedLatLng
            marker.map = naverMap

            naverMap.setOnMapClickListener { pointF, latLng ->

                marker.position = latLng
                marker.map = naverMap

                mSelectedLatLng = latLng

            }

        }

    }

    fun putMyEditAppointmentToServer() {

//        제목
        val inputTitle = binding.edtTitle.text.toString()
        if (inputTitle.isEmpty()){
            Toast.makeText(mContext, "제목이 없습니다. 입력하여 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

//        날짜&시간
        val sdfServer = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val inputDateTime = sdfServer.format(mSelectedAppointmentTime.time)

//        오늘 날짜보다 이전 날짜일 경우 종료
        val today = Calendar.getInstance()
        val sdfDay = SimpleDateFormat("yyyy MM dd")

        if (sdfDay.format(mSelectedAppointmentTime.time).compareTo(sdfDay.format(today.time)) < 0 ){
            Toast.makeText(mContext, "오늘 이후의 날짜로 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

//        도착장소
        val inputPlace = binding.edtPlace.text.toString()
        if (inputPlace.isEmpty()){
            Toast.makeText(mContext, "도착 장소가 없습니다. 입력하여 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val inputLat = mSelectedLatLng.latitude
        val inputLng = mSelectedLatLng.longitude

        Log.d("아이디", mAppointmentData.id.toString())
        Log.d("제목", inputTitle)
        Log.d("날짜", inputDateTime)
        Log.d("도착장소", inputPlace)
        Log.d("위도", inputLat.toString())
        Log.d("경도", inputLng.toString())

        apiList.putRequestEditAppointment(
            mAppointmentData.id,
            inputTitle,
            inputDateTime,
            inputPlace,
            inputLat,
            inputLng
        ).enqueue(object : Callback<BasicResponse>{

            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){
                    Toast.makeText(mContext, "약속 수정에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }

                Log.d("서버 응답", response.toString())

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

    }

}