package com.leejinsil.keepthetime

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityArrivalFriendBinding

class ArrivalFriendActivity : BasePopupActivity() {

    lateinit var binding : ActivityArrivalFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_arrival_friend)
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