package com.leejinsil.keepthetime

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityViewMapGuideBinding

class ViewMapGuideActivity : BaseActivity() {

    lateinit var binding : ActivityViewMapGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_map_guide)
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}