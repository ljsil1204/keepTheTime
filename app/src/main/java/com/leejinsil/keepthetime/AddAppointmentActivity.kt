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
import kotlin.collections.ArrayList

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

    lateinit var mInviteFriendList : ArrayList<UserData>

    val mInviteUserIdList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_appointment)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        ?????? ?????? ????????????
        binding.btnAddFriend.setOnClickListener {

            val myIntent = Intent(mContext, InviteFriendSearchListActivity::class.java)
            startActivityForResult(myIntent, REQ_CODE_INVITE_FRIEND)

        }

//        ???????????? ?????? ????????? - ???????????? ??????
        binding.txtScrollHelp1.setOnTouchListener { view, motionEvent ->

            binding.scrollView.requestDisallowInterceptTouchEvent(true)
            return@setOnTouchListener false

        }

//        ???????????? ?????? ????????? - ?????? ?????? ??????
        binding.txtScrollHelp2.setOnTouchListener { view, motionEvent ->

            binding.scrollView.requestDisallowInterceptTouchEvent(true)
            return@setOnTouchListener false

        }

//        ?????? ?????? -> DatePickerDialog
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

//        ?????? ?????? -> TimePickDialog
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

//        ?????? ?????? -> api
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

//        ???????????? ?????? ????????? ??????
        if (requestCode == REQ_CODE_INVITE_FRIEND) {

            if (resultCode == Activity.RESULT_OK) {

                mInviteFriendList = data?.getParcelableArrayListExtra("invite_selected_friend")!!

                mInviteUserIdList.clear()

                if (mInviteFriendList!!.size > 0) {

                    binding.txtFriend.visibility = View.GONE

//                    ????????? ???????????? ????????? ????????????, visibility ????????????
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

//                    ????????????????????? url ????????????
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
                    binding.txtFriendCount.text = "???????????? ${inviteFriendCount}???"

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

            binding.btnStartPlaceList.background = getDrawable(R.drawable.tab_button_border_default)
            binding.btnStartPlaceEdt.background = getDrawable(R.drawable.tab_button_border_selected)
            binding.startPlaceListContent.visibility = View.GONE
            binding.startPlaceEdtContent.visibility = View.VISIBLE

        }

    }

    fun getTodayFormat () {

//        ?????? ????????? ????????? ???????????? ????????????
        val sdf = SimpleDateFormat("yy/MM/dd (E)")
        binding.btnDay.text = sdf.format(mSelectedAppointmentTime.time)
    }

    fun getNowHourFormat () {

//        ?????? ????????? ????????? ???????????? ????????????
        val sdf = SimpleDateFormat("a h:mm")
        binding.btnHour.text = sdf.format(mSelectedAppointmentTime.time)
    }

    fun getNaverMapView () {

        binding.naverMapView.getMapAsync {

            val naverMap = it

            val coord = LatLng(37.5670135, 126.9783740) // ?????? ??????

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
            Toast.makeText(mContext, "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return
        }

        val sdfServer = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val inputDateTime = sdfServer.format(mSelectedAppointmentTime.time)

        val today = Calendar.getInstance()
        val sdfDay = SimpleDateFormat("yyyy MM dd")

        if (sdfDay.format(mSelectedAppointmentTime.time).compareTo(sdfDay.format(today.time)) < 0 ){
            Toast.makeText(mContext, "?????? ????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return
        }

//        ????????????
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
                Toast.makeText(mContext, "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                return
            }

            if (mStartSelectedLatLng == null) {
                Toast.makeText(mContext, "????????? ???????????? ?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                return
            }

            inputStartLat = mStartSelectedLatLng!!.latitude
            inputStartLng = mStartSelectedLatLng!!.longitude

        }

//        ????????????
        val inputPlace = binding.edtPlace.text.toString()

        if (inputPlace.isEmpty()){
            Toast.makeText(mContext, "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return
        }

        if (mSelectedLatLng == null) {
            Toast.makeText(mContext, "????????? ???????????? ?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return
        }

        val inputLat = mSelectedLatLng!!.latitude
        val inputLng = mSelectedLatLng!!.longitude

        val inviteFriendString = mInviteUserIdList.toString().replace("[","").replace("]","").replace(" ","")

        apiList.postRequestAddAppointment(
            inputTitle,
            inputDateTime,
            inputStartPlace,
            inputStartLat,
            inputStartLng,
            inputPlace,
            inputLat,
            inputLng,
            inviteFriendString
        ).enqueue( object : Callback<BasicResponse>{
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {

                if (response.isSuccessful){

                    val br = response.body()!!
                    val appointmentData : AppointmentData = br.data.appointment

                    Toast.makeText(mContext, "?????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show()

//                    ????????? ?????? ?????? ??? > ???????????? true?????? ??????
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

//        AlarmManager ??????
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

//        receiverIntent ??????
        val receiverIntent = Intent(mContext, MyReceiver::class.java)
        receiverIntent.putExtra("appointment", data)
        receiverIntent.putExtra("cannel_name", CHANNEL_NAME_APPOINTMENT)
        receiverIntent.putExtra("cannel_description", CHANNEL_DESCRIPTION_APPOINTMENT)

//        PendingIntent ??????
        val pendingIntent = PendingIntent.getBroadcast(
            mContext,
            NOTIFICATION_ID,
            receiverIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val sdf = SimpleDateFormat("yy??? MM??? dd??? E?????? a h:mm")
        val alarmTimeFormat = sdf.format(mSelectedTimeCopy.time)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            mSelectedTimeCopy.timeInMillis,
            pendingIntent
        )

        Toast.makeText(mContext, "${alarmTimeFormat}??? ????????? ????????????.", Toast.LENGTH_SHORT).show()

        Log.d("?????? ?????? ??????", mSelectedTimeCopy.time.toString())

    }

    fun saveAlarmInfo(data: AppointmentData) {

        val alarmDescription = "${binding.alarmHourSpinner.selectedItem}?????????."

        AppointmentAlarmContextUtil.setAppointmentId(mContext, data.id)
        AppointmentAlarmContextUtil.setAlarmCheck(mContext, data.id, binding.switchAlarm.isChecked)
        AppointmentAlarmContextUtil.setAlarmSpinnerText(mContext, data.id, binding.alarmHourSpinner.selectedItem.toString())
        AppointmentAlarmContextUtil.setAlarmSpinnerPosition(mContext, data.id, binding.alarmHourSpinner.selectedItemPosition)
        AppointmentAlarmContextUtil.setAlarmReservationTime(mContext, data.id, mSelectedTimeCopy.timeInMillis)
        AppointmentAlarmContextUtil.setAlarmTitle(mContext, data.id, data.title)
        AppointmentAlarmContextUtil.setAlarmDescription(mContext, data.id, alarmDescription)

    }



}