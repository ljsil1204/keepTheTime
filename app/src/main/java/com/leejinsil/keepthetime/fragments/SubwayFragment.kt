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
import com.leejinsil.keepthetime.datas.*
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

    val mSubwayList = ArrayList<PathData>()

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

            override fun onResponse(jsonObj: JSONObject) {

                mSubwayList.clear()

                val pathArr = jsonObj.getJSONArray("path")
                val pathObjList = ArrayList<JSONObject>()
                pathObjList.clear()

                for (i in 0 until pathArr.length()) {
                    val pathObj = pathArr.getJSONObject(i)
                    pathObjList.add(pathObj)
                }

//                val subPathObjList = ArrayList<JSONObject>()
//                subPathObjList.clear()

                for (pathObj in pathObjList) {

                    val pathType = pathObj.getInt("pathType")

                    val subPathArr = pathObj.getJSONArray("subPath")

                    for (i in 0 until subPathArr.length()){
                        val subPathObj = subPathArr.getJSONObject(i)
                        Log.d("지하철", subPathObj.toString())
//                        pathData.subPathList.add(SubPathData.getSubPathDataFromJson(subPathObj))
                    }

                    if (pathType == 1) {

                        Log.d("지하철", pathObj.toString())

//                        val pathData = PathData.getPathDataFromJson(pathObj)

//                        mSubwayList.add(pathData)
//
//                        Log.d("경로", pathObj.toString())

                    }

//                    val subPathArr = pathObj.getJSONArray("subPath")
//
//                    for (i in 0 until subPathArr.length()) {
//
//                        val subPathObj = subPathArr.getJSONObject(i)
//                        subPathObjList.add(subPathObj)
//
//                    }

                }

//                for (subPathObj in subPathObjList) {
//
//                    val trafficType = subPathObj.getInt("trafficType")
//
//                    if (trafficType == 1) {
//
//                        val subPathData = SubPathData.getSubPathDataFromJson(subPathObj)
//                        mSubwayList.add(subPathData)
//
//                    }
//
//                }

                mSubwayAdapter.notifyDataSetChanged()

            }

        })

    }
}