package com.leejinsil.keepthetime.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.fragments.*

class MapDetailViewPager2Adapter(
    fa : FragmentActivity,
    val mAppointmentData : AppointmentData,
) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> SubwayFragment(mAppointmentData)
            1 -> BusFragment(mAppointmentData)
            else -> SubwayPlusBusFragment(mAppointmentData)
        }

    }
}