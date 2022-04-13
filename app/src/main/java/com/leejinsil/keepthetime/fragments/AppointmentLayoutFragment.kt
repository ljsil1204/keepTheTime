package com.leejinsil.keepthetime.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.leejinsil.keepthetime.AddAppointmentActivity
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.adapters.AppointmentLayoutViewPager2Adapter
import com.leejinsil.keepthetime.adapters.FriendViewPager2Adapter
import com.leejinsil.keepthetime.adapters.MapDetailViewPager2Adapter
import com.leejinsil.keepthetime.databinding.FragmentAppointmentLayoutBinding
import com.leejinsil.keepthetime.databinding.FragmentFriendBinding

class AppointmentLayoutFragment : BaseFragment() {

    lateinit var binding : FragmentAppointmentLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_appointment_layout, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnAppointmentAdd.setOnClickListener {

            val myIntent = Intent(mContext, AddAppointmentActivity::class.java)
            startActivity(myIntent)

        }

    }

    override fun setValues() {

        binding.appointmentViewPager2.adapter = AppointmentLayoutViewPager2Adapter(requireActivity())
        binding.appointmentViewPager2.run { isUserInputEnabled = false }
//        binding.friendViewPager2.offscreenPageLimit = 2
        setTabLayout()

    }

    fun setTabLayout() {
        TabLayoutMediator(binding.appointmentTabLayout, binding.appointmentViewPager2, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when(position) {
                0 -> {
                    tab.text = "내가 만든 약속"
                }
                else -> {
                    tab.text = "초대받은 약속"
                }
            }
        }).attach()
    }

}