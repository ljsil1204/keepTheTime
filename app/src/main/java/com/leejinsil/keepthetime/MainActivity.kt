package com.leejinsil.keepthetime

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.leejinsil.keepthetime.adapters.MainViewPager2Adapter
import com.leejinsil.keepthetime.databinding.ActivityMainBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.DataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

    val mNotificationNumData = ArrayList<DataResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        바텀 내비게이션
        binding.mainBottomNav.setOnItemSelectedListener {

            when(it.itemId) {
                R.id.navAppointment -> {
                    binding.mainViewPager2.currentItem = 0
                    actionBarTitle.setText(R.string.actionbar_title_appointment)
                }
                R.id.navFriend -> {
                    binding.mainViewPager2.currentItem = 1
                    actionBarTitle.setText(R.string.actionbar_title_friend)
                }
                R.id.navProfile -> {
                    binding.mainViewPager2.currentItem = 2
                    actionBarTitle.setText(R.string.actionbar_title_profile)
                }
            }

            return@setOnItemSelectedListener true
        }

        binding.mainViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.mainBottomNav.selectedItemId = when(position) {
                    0 -> R.id.navAppointment
                    1 -> R.id.navFriend
                    else -> R.id.navProfile
                }

            }

        })

    }

    override fun setValues() {

        getNotReadNotificationFromServer()

        window.statusBarColor = Color.BLACK
        window.decorView.systemUiVisibility = 0

        actionBarBack.visibility = View.GONE
        actionBarLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black))
        actionBarTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white))
        actionBarNotification.visibility = View.VISIBLE

        binding.mainViewPager2.adapter = MainViewPager2Adapter(this)
        binding.mainViewPager2.offscreenPageLimit = 3

    }

    fun getNotReadNotificationFromServer(){

        apiList.getRequestNotification("false").enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful) {

                    val br = response.body()!!

                    mNotificationNumData.clear()
                    mNotificationNumData.add(br.data)

                    actionBarNotificationNum.text = mNotificationNumData[0].unread_noty_count.toString()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })

    }

    override fun onResume() {
        super.onResume()
        getNotReadNotificationFromServer()
    }

}