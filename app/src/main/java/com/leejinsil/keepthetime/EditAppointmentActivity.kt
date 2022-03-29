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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        mAppointmentData = intent.getSerializableExtra("appointment") as AppointmentData
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        날짜 선택 -> DatePickerDialog
        binding.btnDay.setOnClickListener {

            mSelectedAppointmentTime.time = mAppointmentData.datetime

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
//            binding.btnHour.setOnClickListener {
//
//                val tsl = object : TimePickerDialog.OnTimeSetListener{
//
//                    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
//
//                        mSelectedAppointmentTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
//                        mSelectedAppointmentTime.set(Calendar.MINUTE,minute)
//                        getNowHourFormat()
//                    }
//
//                }
//
//                val tpd = TimePickerDialog(
//                    mContext,
//                    tsl,
//                    mSelectedAppointmentTime.get(Calendar.HOUR_OF_DAY),
//                    mSelectedAppointmentTime.get(Calendar.MINUTE),
//                    false
//                ).show()
//
//            }

        binding.btnAppointmentSave.setOnClickListener {

//        약속 등록 -> api
//            binding.btnAppointmentSave.setOnClickListener {
//                putMyEditAppointmentToServer()
//            }

//            val resultIntent = Intent()
//            resultIntent.putExtra("이름표",  입력값변수)
//            setResult( Activity.RESULT_OK,  resultIntent )
//            finish()

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

            val selectedLatLng = LatLng(mAppointmentData.latitude, mAppointmentData.longitude)

            val cameraUpdate = CameraUpdate.scrollTo(selectedLatLng)
            naverMap.moveCamera(cameraUpdate)

            val marker = Marker()
            marker.position = selectedLatLng
            marker.map = naverMap

        }

    }

//    fun putMyEditAppointmentToServer() {
//
//        val inputTitle = binding.edtTitle.text.toString()
//
////        제목 입력 안 할 시 종료
//        if (inputTitle.isEmpty()){
//            Toast.makeText(mContext, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val sdfServer = SimpleDateFormat("yyyy-MM-dd HH:mm")
//        val inputDateTime = sdfServer.format(mSelectedAppointmentTime.time)
//
////        현재 날짜보다 이전 날짜일 경우 종료
//        val today = Calendar.getInstance()
//        val sdfDay = SimpleDateFormat("yyyy MM dd")
//
//        if (sdfDay.format(mSelectedAppointmentTime.time).compareTo(sdfDay.format(today.time)) < 0 ){
//            Toast.makeText(mContext, "현재 이후의 날짜로 선택해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val inputPlace = binding.edtPlace.text.toString()
//
////        도착 장소 입력 안 할 시 종료
//        if (inputPlace.isEmpty()){
//            Toast.makeText(mContext, "도착 장소를 입력해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
////        네이버 지도 도착 위치 클릭 안할 시
//        if (mSelectedLatLng == null) {
//            Toast.makeText(mContext, "지도를 클릭하여 도착 위치를 설정해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//
//        val inputLat = mSelectedLatLng!!.latitude
//        val inputLng = mSelectedLatLng!!.longitude
//
//        apiList.postRequestAddAppointment(
//            inputTitle,
//            inputDateTime,
//            inputPlace,
//            inputLat,
//            inputLng
//        ).enqueue( object : Callback<BasicResponse> {
//            override fun onResponse(
//                call: Call<BasicResponse>,
//                response: Response<BasicResponse>
//            ) {
//
//                if (response.isSuccessful){
//                    Toast.makeText(mContext, "약속 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//
//            }
//
//            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
//
//            }
//
//        })
//
//    }

}