package com.leejinsil.keepthetime.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.adapters.AppointmentRecyclerViewAdapter
import com.leejinsil.keepthetime.databinding.FragmentAppointmentBinding
import com.leejinsil.keepthetime.datas.AlarmSetAppointmentData
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentInviteFragment : BaseFragment() {

    lateinit var binding : FragmentAppointmentBinding

    val mAppointmentInviteList = ArrayList<AppointmentData>()

    lateinit var mAppointmentInviteListAdapter : AppointmentRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_appointment, container, false)
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

        mAppointmentInviteListAdapter = AppointmentRecyclerViewAdapter(mContext, mAppointmentInviteList)
        binding.appointmentRecyclerView.adapter = mAppointmentInviteListAdapter
        binding.appointmentRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }

    override fun onResume() {
        super.onResume()

        getMyAppointmentFromServer()
    }

    fun getMyAppointmentFromServer() {

        apiList.getRequestMyAppointmentList().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    val br = response.body()!!

                    mAppointmentInviteList.clear()

                    mAppointmentInviteList.addAll(br.data.invited_appointments)

                    mAppointmentInviteListAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

    }

}