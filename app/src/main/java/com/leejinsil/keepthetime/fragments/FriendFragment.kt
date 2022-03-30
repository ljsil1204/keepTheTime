package com.leejinsil.keepthetime.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.adapters.FriendViewPager2Adapter
import com.leejinsil.keepthetime.adapters.MapDetailViewPager2Adapter
import com.leejinsil.keepthetime.databinding.FragmentFriendBinding

class FriendFragment : BaseFragment() {

    lateinit var binding : FragmentFriendBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
    }

    override fun setValues() {

        binding.friendViewPager2.adapter = FriendViewPager2Adapter(requireActivity())
        binding.friendViewPager2.run { isUserInputEnabled = false }
//        binding.friendViewPager2.offscreenPageLimit = 2
        setTabLayout()

    }

    fun setTabLayout() {
        TabLayoutMediator(binding.friendTabLayout, binding.friendViewPager2, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when(position) {
                0 -> {
                    tab.text = "내친구"
                }
                else -> {
                    tab.text = "친구요청"
                }
            }
        }).attach()
    }

}