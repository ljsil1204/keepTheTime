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
import com.leejinsil.keepthetime.databinding.ActivityEditAppointmentBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.PlaceData
import com.leejinsil.keepthetime.datas.UserData
import com.leejinsil.keepthetime.receivers.MyReceiver
import com.leejinsil.keepthetime.receivers.ReceiverConst
import com.leejinsil.keepthetime.utils.AppointmentAlarmContextUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class EditAppointmentActivity : BaseActivity() {

    lateinit var binding : ActivityEditAppointmentBinding

    val REQ_CODE_INVITE_FRIEND = 1000

    lateinit var mAppointmentData : AppointmentData

    val mSelectedAppointmentTime = Calendar.getInstance()
    val mSelectedTimeCopy = Calendar.getInstance()

    lateinit var mSelectedLatLng : LatLng

    lateinit var mStartSelectedLatLng : LatLng

    val mStartPlaceList = ArrayList<PlaceData>()

    lateinit var mStartPlaceSpinnerAdapter : StartPlaceSpinnerAdapter

    val mInviteProfileImage = ArrayList<ImageView>()

    lateinit var mInviteFriendList : ArrayList<UserData>

    val mInviteUserIdList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        mAppointmentData = intent.getSerializableExtra("appointment") as AppointmentData
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

//        선택한 날짜를 받아온 데이터로 대입
        mSelectedAppointmentTime.time = mAppointmentData.datetime

//        날짜 선택 -> DatePickerDialog
        binding.btnDay.setOnClickListener {

            val dsl = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    mSelectedAppointmentTime.set(year, month, dayOfMonth)
                    val sdfDay = SimpleDateFormat("yy/MM/dd (E)")
                    binding.btnDay.text = sdfDay.format(mSelectedAppointmentTime.time)
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
                        val sdfHour = SimpleDateFormat("a h:mm")
                        binding.btnHour.text = sdfHour.format(mSelectedAppointmentTime.time)
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

        binding.alarmHourSpinner.setSelection(AppointmentAlarmContextUtil.getAlarmSpinnerPosition(mContext, mAppointmentData.id))
        binding.switchAlarm.isChecked = AppointmentAlarmContextUtil.getAlarmCheck(mContext, mAppointmentData.id)
        getAlarmHour()

        binding.edtStartPlace.setText(mAppointmentData.start_place)
        binding.edtPlace.setText(mAppointmentData.place)
        getStartNaverMapView()
        getNaverMapView()

        getStartPlaceListFromServer()
        mStartPlaceSpinnerAdapter = StartPlaceSpinnerAdapter(mContext, R.layout.start_place_spinner_list_item, mStartPlaceList)
        binding.startPlaceSpinner.adapter = mStartPlaceSpinnerAdapter

//        초대인원데이터 반영
        mInviteProfileImage.add(binding.inviteFriend1)
        mInviteProfileImage.add(binding.inviteFriend2)
        mInviteProfileImage.add(binding.inviteFriend3)
        mInviteProfileImage.add(binding.inviteFriend4)
        mInviteProfileImage.add(binding.inviteFriend5)

        if (mAppointmentData.invited_friends.size > 1) {

            binding.txtFriend.visibility = View.GONE
            binding.inviteFriendProfile.visibility = View.VISIBLE

            for (i in 0 until mAppointmentData.invited_friends.size) {

                if (i > 4) {
                    break
                }

                Glide.with(mContext).load(mAppointmentData.invited_friends[i].profile_img).into(mInviteProfileImage[i])
                mInviteProfileImage[i].visibility = View.VISIBLE

            }

            var inviteFriendCount = 0

            for (inviteFriend in mAppointmentData.invited_friends) {
                inviteFriendCount++
                mInviteUserIdList.add(inviteFriend.id)
            }

            binding.txtFriendCount.visibility = View.VISIBLE
            binding.txtFriendCount.text = "초대인원 ${inviteFriendCount}명"

            binding.inviteFriendProfile.setOnClickListener {

                val myIntent = Intent(mContext, InviteFriendPopupActivity::class.java)
                myIntent.putExtra("invite_selected", mAppointmentData.invited_friends as Serializable)
                startActivityForResult(myIntent, REQ_CODE_INVITE_FRIEND)

            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        초대친구 추가 데이터 반영
        if (requestCode == REQ_CODE_INVITE_FRIEND) {

            if (resultCode == Activity.RESULT_OK) {

                mInviteFriendList = data?.getParcelableArrayListExtra("invite_selected_friend")!!

                mInviteUserIdList.clear()
                mInviteUserIdList.add(mAppointmentData.user_id)

                if (mInviteFriendList!!.size > 0) {

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
                    for (i in 0 until mInviteFriendList.size) {

                        if (i > 4) {
                            break
                        }

                        Glide.with(mContext).load(mInviteFriendList[i].profile_img).into(mInviteProfileImage[i])
                        mInviteProfileImage[i].visibility = View.VISIBLE

                    }

                    var inviteFriendCount = 0

                    for (inviteFriend in mInviteFriendList) {

                        inviteFriendCount++
                        mInviteUserIdList.add(inviteFriend.id)

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
                    myIntent.putExtra("invite_selected", mInviteFriendList)
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

        val inviteFriendString = mInviteUserIdList.toString().replace("[","").replace("]","").replace(" ","")

        apiList.putRequestEditAppointment(
            mAppointmentData.id,
            inputTitle,
            inputDateTime,
            inputStartPlace,
            inputStartLat,
            inputStartLng,
            inputPlace,
            inputLat,
            inputLng,
            inviteFriendString
        ).enqueue(object : Callback<BasicResponse>{

            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    val br = response.body()!!
                    val appointmentData : AppointmentData = br.data.appointment

//                    서버에 등록 성공 시 > 알람설정
                    val alarmChecked = binding.switchAlarm.isChecked
                    setAlarm(appointmentData, alarmChecked)
                    saveAlarmInfo(appointmentData)

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

    fun setAlarm(data: AppointmentData, check : Boolean) {

//        AlarmManager 생성
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

//        receiverIntent 생성
        val receiverIntent = Intent(mContext, MyReceiver::class.java)
        receiverIntent.putExtra("appointment", data)
        receiverIntent.putExtra("cannel_name", ReceiverConst.CHANNEL_NAME_APPOINTMENT)
        receiverIntent.putExtra("cannel_description", ReceiverConst.CHANNEL_DESCRIPTION_APPOINTMENT)


//        PendingIntent 생성
        val pendingIntent = PendingIntent.getBroadcast(
            mContext,
            ReceiverConst.NOTIFICATION_ID,
            receiverIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val sdf = SimpleDateFormat("yy년 MM월 dd일 E요일 a h:mm")
        val alarmTimeFormat = sdf.format(mSelectedTimeCopy.time)

        if (check) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                mSelectedTimeCopy.timeInMillis,
                pendingIntent
            )
            Toast.makeText(mContext, "${alarmTimeFormat}에 알림이 울립니다.", Toast.LENGTH_SHORT).show()
        }
        else {
            alarmManager.cancel(pendingIntent)
            Toast.makeText(mContext, "알람예약이 취소되었습니다.", Toast.LENGTH_SHORT).show()
        }

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