package com.leejinsil.keepthetime.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.leejinsil.keepthetime.fragments.*

class AppointmentLayoutViewPager2Adapter(fa : FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> AppointmentMyFragment()
            else -> AppointmentInviteFragment()
        }

    }
}