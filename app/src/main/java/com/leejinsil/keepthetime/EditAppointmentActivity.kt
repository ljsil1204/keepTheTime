package com.leejinsil.keepthetime

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.adapters.StartPlaceSpinnerAdapter
import com.leejinsil.keepthetime.databinding.ActivityEditAppointmentBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.PlaceData
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

    lateinit var mSelectedLatLng : LatLng

    lateinit var mStartSelectedLatLng : LatLng

    val mStartPlaceList = ArrayList<PlaceData>()

    lateinit var mStartPlaceSpinnerAdapter : StartPlaceSpinnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        mAppointmentData = intent.getSerializableExtra("appointment") as AppointmentData
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        스크롤뷰 터치 이벤트 - 출발장소 지도
        binding.txtScrollHelp1.setOnTouchListener { view, motionEvent ->

            binding.scrollView.requestDisallowInterceptTouchEvent(true)
            return@setOnTouchListener false

        }

//        스크롤뷰 터치 이벤트 - 도착 장소 지도
        binding.txtScrollHelp2.setOnTouchListener { view, motionEvent ->

            binding.scrollView.requestDisallowInterceptTouchEvent(true)
            return@setOnTouchListener false

        }

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

//        약속 수정 api
        binding.btnAppointmentSave.setOnClickListener {

            putMyEditAppointmentToServer()

        }

    }

    override fun setValues() {

        actionBarTitle.setText(R.string.actionbar_title_edit_appointment)

        viewStartPlaceLayout()

        binding.edtTitle.setText(mAppointmentData.title)
        getDayFormat()
        getHourFormat()
        binding.edtStartPlace.setText(mAppointmentData.start_place)
        binding.edtPlace.setText(mAppointmentData.place)
        getStartNaverMapView()
        getNaverMapView()

        getStartPlaceListFromServer()
        mStartPlaceSpinnerAdapter = StartPlaceSpinnerAdapter(mContext, R.layout.start_place_spinner_list_item, mStartPlaceList)
        binding.startPlaceSpinner.adapter = mStartPlaceSpinnerAdapter

    }

    fun viewStartPlaceLayout(){

        binding.btnStartPlaceList.setOnClickListener {

            binding.btnStartPlaceList.background = getDrawable(R.drawable.tab_button_border_selected)
            binding.btnStartPlaceEdt.background = getDrawable(R.drawable.tab_button_border_default)
            binding.startPlaceListContent.visibility = View.VISIBLE
            binding.startPlaceEdtContent.visibility = View.GONE

        }

        binding.btnStartPlaceEdt.setOnClickListener {

            binding.btnStartPlaceEdt.background = getDrawable(R.drawable.tab_button_border_selected)
            binding.btnStartPlaceList.background = getDrawable(R.drawable.tab_button_border_default)
            binding.startPlaceListContent.visibility = View.GONE
            binding.startPlaceEdtContent.visibility = View.VISIBLE

        }

    }

    fun getDayFormat () {

        val sdfDay = SimpleDateFormat("yy/MM/dd (E)")
        binding.btnDay.text = sdfDay.format(mAppointmentData.datetime.time)

    }

    fun getHourFormat () {

        val sdfHour = SimpleDateFormat("a h:mm")
        binding.btnHour.text = sdfHour.format(mAppointmentData.datetime.time)

    }

    fun getStartNaverMapView () {

        binding.naverMapViewStart.getMapAsync {

            val naverMap = it

            mStartSelectedLatLng = LatLng(mAppointmentData.start_latitude, mAppointmentData.start_longitude)

            val cameraUpdate = CameraUpdate.scrollTo(mStartSelectedLatLng)
            naverMap.moveCamera(cameraUpdate)

            val marker = Marker()
            marker.position = mStartSelectedLatLng
            marker.map = naverMap

            naverMap.setOnMapClickListener { pointF, latLng ->

                marker.position = latLng
                marker.map = naverMap

                mStartSelectedLatLng = latLng

            }


        }

    }

    fun getNaverMapView () {

        binding.naverMapView.getMapAsync {

            val naverMap = it

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

        val today = Calendar.getInstance()
        val sdfDay = SimpleDateFormat("yyyy MM dd")

        if (sdfDay.format(mSelectedAppointmentTime.time).compareTo(sdfDay.format(today.time)) < 0 ){
            Toast.makeText(mContext, "오늘 이후의 날짜로 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

//        출발장소
        var inputStartPlace : String
        var inputStartLat : Double
        var inputStartLng : Double

        if (binding.startPlaceListContent.visibility == View.VISIBLE) {

            inputStartPlace = mStartPlaceList[binding.startPlaceSpinner.selectedItemPosition].name
            inputStartLat = mStartPlaceList[binding.startPlaceSpinner.selectedItemPosition].latitude
            inputStartLng = mStartPlaceList[binding.startPlaceSpinner.selectedItemPosition].longitude

        } else{

            inputStartPlace = binding.edtStartPlace.text.toString()

            if (inputStartPlace.isEmpty()){
                Toast.makeText(mContext, "출발 장소가 없습니다. 입력하여 주세요.", Toast.LENGTH_SHORT).show()
                return
            }

            inputStartLat = mStartSelectedLatLng.latitude
            inputStartLng = mStartSelectedLatLng.longitude

        }

//        도착장소
        val inputPlace = binding.edtPlace.text.toString()
        if (inputPlace.isEmpty()){
            Toast.makeText(mContext, "도착 장소가 없습니다. 입력하여 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val inputLat = mSelectedLatLng.latitude
        val inputLng = mSelectedLatLng.longitude

        apiList.putRequestEditAppointment(
            mAppointmentData.id,
            inputTitle,
            inputDateTime,
            inputStartPlace,
            inputStartLat,
            inputStartLng,
            inputPlace,
            inputLat,
            inputLng
        ).enqueue(object : Callback<BasicResponse>{

            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    Toast.makeText(mContext, "약속 수정에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    finish()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

        postStartPlaceToServer()

    }

    fun postStartPlaceToServer(){

        val inputStartPlace = binding.edtStartPlace.text.toString()

        var isCheckedAddStartPlace = binding.checkAddStartPlaceList.isChecked

        binding.checkAddStartPlaceList.setOnCheckedChangeListener { compoundButton, b ->
            isCheckedAddStartPlace = b
        }

        var isCheckedDefaultStartPlace = binding.checkSelectedDefaultStartPlace.isChecked

        binding.checkSelectedDefaultStartPlace.setOnCheckedChangeListener { compoundButton, b ->
            isCheckedDefaultStartPlace = b
        }

        if (isCheckedAddStartPlace){

            apiList.postRequestAddPlace(
                inputStartPlace,
                mStartSelectedLatLng!!.latitude,
                mStartSelectedLatLng!!.longitude,
                isCheckedDefaultStartPlace
            ).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }
            })

        }

    }

    fun getStartPlaceListFromServer() {

        apiList.getRequestPlaceList().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    val br = response.body()!!

                    mStartPlaceList.clear()

                    mStartPlaceList.addAll(br.data.places)

                    mStartPlaceSpinnerAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }
        })

    }

}