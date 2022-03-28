package com.leejinsil.keepthetime.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.ViewMapGuideActivity
import com.leejinsil.keepthetime.adapters.PathListRecyclerViewAdapter
import com.leejinsil.keepthetime.databinding.FragmentSubwayBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.InfoData
import com.leejinsil.keepthetime.datas.LaneData
import com.leejinsil.keepthetime.datas.SubPathData
import com.leejinsil.keepthetime.utils.ODsayServerUtil
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
import org.json.JSONObject

class SubwayFragment(
    val mAppointmentData: AppointmentData,
) : BaseFragment() {

    lateinit var binding : FragmentSubwayBinding

    val mSubwayList = ArrayList<SubPathData>()
    val mLaneData = ArrayList<LaneData>()

    lateinit var mSubwayAdapter : PathListRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subway, container, false)
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

        mSubwayAdapter = PathListRecyclerViewAdapter(mContext, mSubwayList, mAppointmentData)
        binding.subwayRecyclerView.adapter = mSubwayAdapter
        binding.subwayRecyclerView.layoutManager = LinearLayoutManager(mContext)

        getSubwayPathFromServer()

    }


    fun getSubwayPathFromServer() {

        ODsayServerUtil.getRequestSearchPubTransPath(mContext, mAppointmentData, object : ODsayServerUtil.JsonResponseHandler{

            override fun onResponse(jsonObj: JSONObject) {

                mSubwayList.clear()

                val pathArr = jsonObj.getJSONArray("path")
                val pathObjList = ArrayList<JSONObject>()
                pathObjList.clear()

                for (i in 0 until pathArr.length()) {
                    val pathObj = pathArr.getJSONObject(i)
                    pathObjList.add(pathObj)
                    Log.d("pathObj 경로", pathObj.toString())
                }

                val subPathObjList = ArrayList<JSONObject>()
                subPathObjList.clear()

                for (pathObj in pathObjList) {

                    val subPathArr = pathObj.getJSONArray("subPath")

                    for (i in 0 until subPathArr.length()) {

                        val subPathObj = subPathArr.getJSONObject(i)
                        subPathObjList.add(subPathObj)

                    }

                }

                for (subPathObj in subPathObjList) {

                    val trafficType = subPathObj.getInt("trafficType")

                    if (trafficType == 1) {

                        val subPathData = SubPathData.getSubPathDataFromJson(subPathObj)
                        mSubwayList.add(subPathData)

                    }

                }

                mSubwayAdapter.notifyDataSetChanged()

            }

        })

    }
}