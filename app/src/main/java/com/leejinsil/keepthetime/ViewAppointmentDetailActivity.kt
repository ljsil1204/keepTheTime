package com.leejinsil.keepthetime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.leejinsil.keepthetime.databinding.ActivityViewAppointmentDetailBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.BasicResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.overlay.Marker
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class ViewAppointmentDetailActivity : BaseActivity() {

    lateinit var binding : ActivityViewAppointmentDetailBinding

    lateinit var mAppointmentData : AppointmentData

    var marker : Marker? = null

    val mInviteProfileImage = ArrayList<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_appointment_detail)
        mAppointmentData = intent.getSerializableExtra("appointment") as AppointmentData
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnMap.setOnClickListener {

            val myIntent = Intent(mContext, TransitPathListActivity::class.java)
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

        binding.imgRefresh.setOnClickListener {
            getAppointmentDetailFormServer()
        }

    }

    override fun setValues() {

        actionBarTitle.setText(R.string.actionbar_title_detail_appointment)

        setUi()

    }

    fun setUi() {

        mInviteProfileImage.add(binding.inviteFriend1)
        mInviteProfileImage.add(binding.inviteFriend2)
        mInviteProfileImage.add(binding.inviteFriend3)
        mInviteProfileImage.add(binding.inviteFriend4)
        mInviteProfileImage.add(binding.inviteFriend5)


        if (mAppointmentData.invited_friends.size > 1) {

            binding.inviteLayout.visibility = View.VISIBLE

            for (i in 0 until mAppointmentData.invited_friends.size) {

                Glide.with(mContext).load(mAppointmentData.invited_friends[i].profile_img).into(mInviteProfileImage[i])
                mInviteProfileImage[i].visibility = View.VISIBLE

            }

            var arrivalCount = 0

            for (inviteFriend in mAppointmentData.invited_friends) {

                if (inviteFriend.arrived_at != null) {
                    arrivalCount++
                }

            }
            binding.txtArrivalCount.text = "도착인원 ${arrivalCount}명"

        }

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

    fun postAppointmentArrivalToServer() {

        apiList.postRequestAppointmentArrival(
            mAppointmentData.id,
            mAppointmentData.latitude,
            mAppointmentData.longitude
        ).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){
                    Toast.makeText(mContext, "약속 도착 인증 성공!", Toast.LENGTH_SHORT).show()
                    getAppointmentDetailFormServer()
                }
                else {

                    val br = response.errorBody()!!.string()
                    val jsonObj = JSONObject(br)
                    val message = jsonObj.getString("message")
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })

    }

    override fun onResume() {
        super.onResume()

        getAppointmentDetailFormServer()

        binding.btnArrival.setOnClickListener {
            postAppointmentArrivalToServer()
        }

    }

}