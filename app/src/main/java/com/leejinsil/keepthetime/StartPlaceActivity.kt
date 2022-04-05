package com.leejinsil.keepthetime

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityStartPlaceBinding

class StartPlaceActivity : BaseActivity() {

    lateinit var binding : ActivityStartPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_place)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
    }
}