package com.leejinsil.keepthetime

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityViewMapDetailBinding

class ViewMapDetailActivity : BaseActivity() {

    lateinit var binding : ActivityViewMapDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_map_detail)
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}