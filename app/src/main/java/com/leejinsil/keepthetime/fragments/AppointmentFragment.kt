package com.leejinsil.keepthetime.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.adapters.AppointmentRecyclerViewAdapter
import com.leejinsil.keepthetime.databinding.FragmentAppointmentBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentFragment : BaseFragment() {

    lateinit var binding : FragmentAppointmentBinding

    val mAppointmentList = ArrayList<AppointmentData>()

    lateinit var mAppointmentListAdapter : AppointmentRecyclerViewAdapter

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

        mAppointmentListAdapter = AppointmentRecyclerViewAdapter(mContext, mAppointmentList)
        binding.appointmentRecyclerView.adapter = mAppointmentListAdapter
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

                    mAppointmentList.clear()

                    mAppointmentList.addAll(br.data.appointments)

                    mAppointmentListAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

    }

}