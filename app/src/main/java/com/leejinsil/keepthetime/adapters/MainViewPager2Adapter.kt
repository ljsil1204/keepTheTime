package com.leejinsil.keepthetime.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.leejinsil.keepthetime.fragments.AppointmentFragment
import com.leejinsil.keepthetime.fragments.FriendFragment
import com.leejinsil.keepthetime.fragments.MyProfileFragment

class MainViewPager2Adapter( fa : FragmentActivity  ) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> AppointmentFragment()
            1 -> FriendFragment()
            else -> MyProfileFragment()
        }

    }
}