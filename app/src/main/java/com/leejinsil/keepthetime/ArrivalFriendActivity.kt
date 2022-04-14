package com.leejinsil.keepthetime

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityArrivalFriendBinding
import com.leejinsil.keepthetime.datas.AppointmentData

class ArrivalFriendActivity : BasePopupActivity() {

    lateinit var binding : ActivityArrivalFriendBinding

    lateinit var mAppointmentData : AppointmentData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_arrival_friend)
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

    }

}