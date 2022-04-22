package com.leejinsil.keepthetime

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.leejinsil.keepthetime.adapters.StartPlaceSpinnerAdapter
import com.leejinsil.keepthetime.databinding.ActivityAddAppointmentBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.PlaceData
import com.leejinsil.keepthetime.datas.UserData
import com.leejinsil.keepthetime.receivers.MyReceiver
import com.leejinsil.keepthetime.receivers.ReceiverConst.Companion.CHANNEL_DESCRIPTION_APPOINTMENT
import com.leejinsil.keepthetime.receivers.ReceiverConst.Companion.CHANNEL_NAME_APPOINTMENT
import com.leejinsil.keepthetime.receivers.ReceiverConst.Companion.NOTIFICATION_ID
import com.leejinsil.keepthetime.utils.AppointmentAlarmContextUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AddAppointmentActivity : BaseActivity() {

    lateinit var binding : ActivityAddAppointmentBinding

    val REQ_CODE_INVITE_FRIEND = 1000

    val mSelectedAppointmentTime = Calendar.getInstance()
    val mSelectedTimeCopy = Calendar.getInstance()

    var marker : Marker? = null

    var startMarker : Marker? = null

    var mSelectedLatLng : LatLng? = null

    var mStartSelectedLatLng : LatLng? = null

    val mStartPlaceList = ArrayList<PlaceData>()

    lateinit var mStartPlaceSpinnerAdapter : StartPlaceSpinnerAdapter

    val mInviteProfileImage = ArrayList<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_appointment)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        초대 친구 화면이동
        binding.btnAddFriend.setOnClickListener {

            val myIntent = Intent(mContext, InviteFriendSearchListActivity::class.java)
            startActivityForResult(myIntent, REQ_CODE_INVITE_FRIEND)

        }

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

//        날짜 선택 -> DatePickerDialog
        binding.btnDay.setOnClickListener {

            val dsl = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    mSelectedAppointmentTime.set(year, month, dayOfMonth)
                    getTodayFormat()
                    getAlarmHour()

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
                    mSelectedAppointmentTime.set(Calendar.SECOND,0)
                    getNowHourFormat()
                    getAlarmHour()
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

        actionBarTitle.setText(R.string.actionbar_title_add_appointment)

        getTodayFormat()
        getNowHourFormat()
        getAlarmHour()
        getNaverMapView()
        getStartNaverMapView()
        viewStartPlaceLayout()
        getStartPlaceListFromServer()

        mStartPlaceSpinnerAdapter = StartPlaceSpinnerAdapter(mContext, R.layout.start_place_spinner_list_item, mStartPlaceList)
        binding.startPlaceSpinner.adapter = mStartPlaceSpinnerAdapter

        mInviteProfileImage.add(binding.inviteFriend1)
        mInviteProfileImage.add(binding.inviteFriend2)
        mInviteProfileImage.add(binding.inviteFriend3)
        mInviteProfileImage.add(binding.inviteFriend4)
        mInviteProfileImage.add(binding.inviteFriend5)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        초대친구 추가 데이터 반영
        if (requestCode == REQ_CODE_INVITE_FRIEND) {

            if (resultCode == Activity.RESULT_OK) {

                val inviteFriendList = data?.getParcelableArrayListExtra<UserData>("invite_selected_friend")

                if (inviteFriendList!!.size > 0) {

                    binding.txtFriend.visibility = View.GONE

//                    프로필 이미지가 보이는 경우일때, visibility 분기처리
                    if (binding.inviteFriendProfile.visibility == View.VISIBLE) {

                        for ( inviteProfile in mInviteProfileImage){

                            if (inviteProfile.visibility == View.VISIBLE) {
                                inviteProfile.visibility = View.GONE
                            }

                        }

                    }
                    else {
                        binding.inviteFriendProfile.visibility = View.VISIBLE
                    }

//                    프로필이미지에 url 넣어주기
                    for (i in 0 until inviteFriendList.size) {

                        if (i > 4) {
                            break
                        }

                        Glide.with(mContext).load(inviteFriendList[i].profile_img).into(mInviteProfileImage[i])
                        mInviteProfileImage[i].visibility = View.VISIBLE

                    }

                    var inviteFriendCount = 0

                    for (inviteFriend in inviteFriendList) {
                        inviteFriendCount++
                    }

                    binding.txtFriendCount.visibility = View.VISIBLE
                    binding.txtFriendCount.text = "초대인원 ${inviteFriendCount}명"

                }
                else {

                    binding.txtFriend.visibility = View.VISIBLE
                    binding.inviteFriendProfile.visibility = View.GONE
                    binding.txtFriendCount.visibility = View.GONE

                }

                binding.inviteFriendProfile.setOnClickListener {

                    val myIntent = Intent(mContext, InviteFriendPopupActivity::class.java)
                    myIntent.putExtra("invite_selected", inviteFriendList)
                    startActivityForResult(myIntent, REQ_CODE_INVITE_FRIEND)

                }

            }

        }

    }

    fun viewStartPlaceLayout(){

        binding.btnStartPlaceList.setOnClickListener {

            binding.btnStartPlaceList.background = getDrawable(R.drawable.tab_button_border_selected)
            binding.btnStartPlaceEdt.background = getDrawable(R.drawable.tab_button_border_default)
            binding.startPlaceListContent.visibility = View.VISIBLE
            binding.startPlaceEdtContent.visibility = View.GONE

        }

        binding.btnStartPlaceEdt.setOnClickListener {

            binding.btnStartPlaceList.background = getDrawable(R.drawable.tab_button_border_default)
            binding.btnStartPlaceEdt.background = getDrawable(R.drawable.tab_button_border_selected)
            binding.startPlaceListContent.visibility = View.GONE
            binding.startPlaceEdtContent.visibility = View.VISIBLE

        }

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

                if (startMarker == null) {
                    startMarker = Marker()
                }

                startMarker!!.position = latLng
                startMarker!!.map = naverMap

                mStartSelectedLatLng = latLng

            }


        }

    }

    fun postMyAppointmentToServer() {

        val inputTitle = binding.edtTitle.text.toString()

        if (inputTitle.isEmpty()){
            Toast.makeText(mContext, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val sdfServer = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val inputDateTime = sdfServer.format(mSelectedAppointmentTime.time)

        val today = Calendar.getInstance()
        val sdfDay = SimpleDateFormat("yyyy MM dd")

        if (sdfDay.format(mSelectedAppointmentTime.time).compareTo(sdfDay.format(today.time)) < 0 ){
            Toast.makeText(mContext, "현재 이후의 날짜로 선택해주세요.", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(mContext, "출발 장소를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return
            }

            if (mStartSelectedLatLng == null) {
                Toast.makeText(mContext, "지도를 클릭하여 출발 위치를 설정해주세요.", Toast.LENGTH_SHORT).show()
                return
            }

            inputStartLat = mStartSelectedLatLng!!.latitude
            inputStartLng = mStartSelectedLatLng!!.longitude

        }

//        도착장소
        val inputPlace = binding.edtPlace.text.toString()

        if (inputPlace.isEmpty()){
            Toast.makeText(mContext, "도착 장소를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

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

                    val br = response.body()!!
                    val appointmentData : AppointmentData = br.data.appointment

                    Toast.makeText(mContext, "약속 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show()

//                    서버에 등록 성공 시 > 알람설정 true이면 실행
                    if (binding.switchAlarm.isChecked) {
                        setAlarm(appointmentData)
                    }

                    saveAlarmInfo(appointmentData)

                    finish()
                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

        postStartPlaceToServer()

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

    fun getAlarmHour(){

        mSelectedTimeCopy.time = mSelectedAppointmentTime.time

        var currentSpinner = binding.alarmHourSpinner.selectedItemPosition

        when(currentSpinner){
            0 -> {
                mSelectedTimeCopy
            }
            1 -> {
                mSelectedTimeCopy.add(Calendar.MINUTE, -5)
            }
            2 -> {
                mSelectedTimeCopy.add(Calendar.MINUTE, -10)
            }
            3 -> {
                mSelectedTimeCopy.add(Calendar.MINUTE, -15)
            }
            4 -> {
                mSelectedTimeCopy.add(Calendar.MINUTE, -30)
            }
            5 -> {
                mSelectedTimeCopy.add(Calendar.HOUR_OF_DAY, -1)
            }
            else -> {
                mSelectedTimeCopy.add(Calendar.HOUR_OF_DAY, -2)
            }
        }

        binding.alarmHourSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                mSelectedTimeCopy.time = mSelectedAppointmentTime.time

                when(position){
                    0 -> {
                        mSelectedTimeCopy
                        currentSpinner = position
                    }
                    1 -> {
                        mSelectedTimeCopy.add(Calendar.MINUTE, -5)
                        currentSpinner = position
                    }
                    2 -> {
                        mSelectedTimeCopy.add(Calendar.MINUTE, -10)
                        currentSpinner = position
                    }
                    3 -> {
                        mSelectedTimeCopy.add(Calendar.MINUTE, -15)
                        currentSpinner = position
                    }
                    4 -> {
                        mSelectedTimeCopy.add(Calendar.MINUTE, -30)
                        currentSpinner = position
                    }
                    5 -> {
                        mSelectedTimeCopy.add(Calendar.HOUR_OF_DAY, -1)
                        currentSpinner = position
                    }
                    else -> {
                        mSelectedTimeCopy.add(Calendar.HOUR_OF_DAY, -2)
                        currentSpinner = position
                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

    }

    fun setAlarm(data: AppointmentData) {

//        AlarmManager 생성
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

//        receiverIntent 생성
        val receiverIntent = Intent(mContext, MyReceiver::class.java)
        receiverIntent.putExtra("appointment", data)
        receiverIntent.putExtra("cannel_name", CHANNEL_NAME_APPOINTMENT)
        receiverIntent.putExtra("cannel_description", CHANNEL_DESCRIPTION_APPOINTMENT)

//        PendingIntent 생성
        val pendingIntent = PendingIntent.getBroadcast(
            mContext,
            NOTIFICATION_ID,
            receiverIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val sdf = SimpleDateFormat("yy년 MM월 dd일 E요일 a h:mm")
        val alarmTimeFormat = sdf.format(mSelectedTimeCopy.time)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            mSelectedTimeCopy.timeInMillis,
            pendingIntent
        )

        Toast.makeText(mContext, "${alarmTimeFormat}에 알림이 울립니다.", Toast.LENGTH_SHORT).show()

        Log.d("알람 예약 시간", mSelectedTimeCopy.time.toString())

    }

    fun saveAlarmInfo(data: AppointmentData) {

        val alarmDescription = "${binding.alarmHourSpinner.selectedItem}입니다."

        AppointmentAlarmContextUtil.setAppointmentId(mContext, data.id)
        AppointmentAlarmContextUtil.setAlarmCheck(mContext, data.id, binding.switchAlarm.isChecked)
        AppointmentAlarmContextUtil.setAlarmSpinnerText(mContext, data.id, binding.alarmHourSpinner.selectedItem.toString())
        AppointmentAlarmContextUtil.setAlarmSpinnerPosition(mContext, data.id, binding.alarmHourSpinner.selectedItemPosition)
        AppointmentAlarmContextUtil.setAlarmReservationTime(mContext, data.id, mSelectedTimeCopy.timeInMillis)
        AppointmentAlarmContextUtil.setAlarmTitle(mContext, data.id, data.title)
        AppointmentAlarmContextUtil.setAlarmDescription(mContext, data.id, alarmDescription)

    }



}