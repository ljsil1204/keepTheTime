package com.leejinsil.keepthetime

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.leejinsil.keepthetime.adapters.MapDetailViewPager2Adapter
import com.leejinsil.keepthetime.databinding.ActivityViewMapDetailBinding
import com.leejinsil.keepthetime.datas.AppointmentData

class ViewMapDetailActivity : BaseActivity() {

    lateinit var binding : ActivityViewMapDetailBinding

    lateinit var mAppointmentData : AppointmentData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_map_detail)
        mAppointmentData = intent.getSerializableExtra("appointment") as AppointmentData
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        binding.mapDetailViewPager2.adapter = MapDetailViewPager2Adapter(this)
        binding.mapDetailViewPager2.offscreenPageLimit = 3
        TabLayoutMediator(binding.mapDetailTabLayout, binding.mapDetailViewPager2, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when(position) {
                0 -> {
//                    tab.text = "지하철"
                    tab.icon = getDrawable(R.drawable.tablayout_icon_subway)
                }
                1 -> {
//                    tab.text = "버스"
                    tab.icon = getDrawable(R.drawable.tablayout_icon_bus)
                }
                else -> {
//                    tab.text = "도보"
                    tab.icon = getDrawable(R.drawable.tablayout_icon_walk)
                }
            }
        }).attach()
    }
}