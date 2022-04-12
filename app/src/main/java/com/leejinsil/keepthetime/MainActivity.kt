package com.leejinsil.keepthetime

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.leejinsil.keepthetime.adapters.MainViewPager2Adapter
import com.leejinsil.keepthetime.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

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

        window.statusBarColor = Color.BLACK
        window.decorView.systemUiVisibility = 0

        actionBarBack.visibility = View.GONE
        actionBarLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black))
        actionBarTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white))
        actionBarNotification.visibility = View.VISIBLE

        binding.mainViewPager2.adapter = MainViewPager2Adapter(this)
        binding.mainViewPager2.offscreenPageLimit = 3

    }
}