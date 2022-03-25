package com.leejinsil.keepthetime.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.databinding.FragmentSubwayBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.odsay.odsayandroidsdk.ODsayData

class SubwayFragment : BaseFragment() {

    lateinit var binding : FragmentSubwayBinding

//    val mAppointmentData : AppointmentData by lazy { arguments?.getSerializable("appointment") as AppointmentData}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subway, container, false)

        val getstr = arguments?.getString("Key")
        Log.d("테스트", getstr.toString())

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

//        if (arguments == null) {
//            Log.d("data : ", "null")
//        }
//        else{
//            Log.d("data : ", mAppointmentData.title)
//        }
    }
}