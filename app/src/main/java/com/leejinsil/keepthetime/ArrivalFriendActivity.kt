package com.leejinsil.keepthetime

import android.app.Activity
import android.os.Bundle
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityArrivalFriendBinding

class ArrivalFriendActivity : Activity() {

    lateinit var binding : ActivityArrivalFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_arrival_friend)
        setContentView(R.layout.activity_arrival_friend)
    }

}