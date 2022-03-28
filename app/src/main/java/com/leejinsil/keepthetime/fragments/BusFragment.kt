package com.leejinsil.keepthetime.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.adapters.PathListRecyclerViewAdapter
import com.leejinsil.keepthetime.databinding.FragmentBusBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.PathData
import com.leejinsil.keepthetime.utils.ODsayServerUtil
import org.json.JSONObject

class BusFragment(
    val mAppointmentData: AppointmentData,
) : BaseFragment() {

    lateinit var binding : FragmentBusBinding

    val mBusList = ArrayList<PathData>()

    lateinit var mBusAdapter : PathListRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bus, container, false)
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

        mBusAdapter = PathListRecyclerViewAdapter(mContext, mBusList, mAppointmentData)
        binding.busRecyclerView.adapter = mBusAdapter
        binding.busRecyclerView.layoutManager = LinearLayoutManager(mContext)

        getBusFromServer()

    }

    fun getBusFromServer() {

        ODsayServerUtil.getRequestSearchPubTransPath(mContext, mAppointmentData, object : ODsayServerUtil.JsonResponseHandler{

            override fun onResponse(jsonObj: JSONObject) {

                mBusList.clear()

                val pathArr = jsonObj.getJSONArray("path")
                val pathObjList = ArrayList<JSONObject>()
                pathObjList.clear()

                for (i in 0 until pathArr.length()) {
                    val pathObj = pathArr.getJSONObject(i)
                    pathObjList.add(pathObj)
                }

                for (pathObj in pathObjList) {

                    val pathType = pathObj.getInt("pathType")

                    if (pathType == 2) {

                        val pathData = PathData.getPathDataFromJson(pathObj)
                        mBusList.add(pathData)
                    }

                }

                mBusAdapter.notifyDataSetChanged()

            }

        })

    }

}