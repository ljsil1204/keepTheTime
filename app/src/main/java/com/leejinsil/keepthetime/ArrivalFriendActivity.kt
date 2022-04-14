package com.leejinsil.keepthetime

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class ArrivalFriendActivity : AppCompatActivity() {

//    lateinit var binding : ActivityArrivalFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_arrival_friend)
        setContentView(R.layout.activity_arrival_friend)
    }

}