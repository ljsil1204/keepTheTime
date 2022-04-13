package com.leejinsil.keepthetime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.adapters.MainViewPager2Adapter
import com.leejinsil.keepthetime.adapters.NotificationRecyclerViewAdapter
import com.leejinsil.keepthetime.databinding.ActivityNotificationBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.NotificationData
import com.leejinsil.keepthetime.fragments.FriendRequestedFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivity : BaseActivity() {

    lateinit var binding : ActivityNotificationBinding

    val mNotificationList = ArrayList<NotificationData>()

    lateinit var mNotificationAdapter : NotificationRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.allRead.setOnClickListener {
            postNotificationAllReadToServer()
        }


    }

    override fun setValues() {

        actionBarTitle.setText(R.string.actionbar_title_notification)

        mNotificationAdapter = NotificationRecyclerViewAdapter(mContext, mNotificationList)
        binding.notificationRecyclerView.adapter = mNotificationAdapter
        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(mContext)

        mNotificationAdapter.setItemClickListener(object : NotificationRecyclerViewAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {

            }

            override fun onClickDataType(view: View, position: Int, type: String) {
                when(type){
                    "친구추가요청" -> Toast.makeText(mContext, "친구추가알림", Toast.LENGTH_SHORT).show()
                    "약속초대" -> Toast.makeText(mContext, "약속초대알림", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    fun getNotificationFromServer(){

        apiList.getRequestNotification("true").enqueue( object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    val br = response.body()!!

                    mNotificationList.clear()
                    mNotificationList.addAll(br.data.notifications)

                    mNotificationAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })

    }

    fun postNotificationAllReadToServer(){

        apiList.postRequestNotificationRead(mNotificationList[0].id).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    mNotificationAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })

    }

    override fun onResume() {
        super.onResume()
        getNotificationFromServer()
    }

}




