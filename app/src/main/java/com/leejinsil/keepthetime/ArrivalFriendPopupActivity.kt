package com.leejinsil.keepthetime

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.adapters.ArrivalFriendRecyclerAdapter
import com.leejinsil.keepthetime.databinding.ActivityArrivalFriendPopupBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArrivalFriendPopupActivity : BasePopupActivity() {

    lateinit var binding : ActivityArrivalFriendPopupBinding

    lateinit var mAppointmentData : AppointmentData

    val mInviteUserList = ArrayList<UserData>()

    lateinit var mArrivalFriendAdapter : ArrivalFriendRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_arrival_friend_popup)
        mAppointmentData = intent.getSerializableExtra("appointment") as AppointmentData
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.popupClose.setOnClickListener {
            finish()
        }

    }

    override fun setValues() {

        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mArrivalFriendAdapter = ArrivalFriendRecyclerAdapter(mContext, mInviteUserList)
        binding.friendArrivalRecycleView.adapter = mArrivalFriendAdapter
        binding.friendArrivalRecycleView.layoutManager = LinearLayoutManager(mContext)

    }

    fun getAppointmentDetailFormServer() {

        apiList.getRequestMyAppointmentDetail(mAppointmentData.id.toString()).enqueue( object :
            Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful) {

                    val br = response.body()!!

                    mInviteUserList.clear()
                    mInviteUserList.addAll(br.data.appointment.invited_friends)

                    mArrivalFriendAdapter.notifyDataSetChanged()

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