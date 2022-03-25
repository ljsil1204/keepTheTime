package com.leejinsil.keepthetime.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.adapters.PathListRecyclerViewAdapter
import com.leejinsil.keepthetime.databinding.FragmentSubwayBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.PathData
import com.leejinsil.keepthetime.datas.SubPathData
import com.leejinsil.keepthetime.utils.ODsayServerUtil
import com.naver.maps.geometry.LatLng
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

        mSubwayAdapter = PathListRecyclerViewAdapter(mContext, mSubwayList)
        binding.subwayRecyclerView.adapter = mSubwayAdapter
        binding.subwayRecyclerView.layoutManager = LinearLayoutManager(mContext)

        getSubwayPathFromServer()

    }


    fun getSubwayPathFromServer() {

        ODsayServerUtil.getRequestSearchPubTransPath(mContext, mAppointmentData, object : ODsayServerUtil.JsonResponseHandler{

            override fun onResponse( jsonObj : JSONObject) {

                Log.d("대중교통 서버 응답", "응답성공")
                Toast.makeText(mContext, "토스트", Toast.LENGTH_SHORT).show()

                val pathArr = jsonObj.getJSONArray("path")

                for (i in 0 until pathArr.length()) {
                    val pathObj = pathArr.getJSONObject(i)

                    Log.d("pathObj", pathObj.toString())

                    val subpathData = SubPathData.getSubPathDataFromJson(pathObj)

                    Log.d("시작정류장", subpathData.startName)
//
//                    if ( subpathData.trafficType == 1) {
//                        mSubwayList.add(subpathData)
//                    }
                }

                mSubwayAdapter.notifyDataSetChanged()

            }

        })

    }

}