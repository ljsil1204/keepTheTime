package com.leejinsil.keepthetime.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.adapters.PathListRecyclerViewAdapter
import com.leejinsil.keepthetime.databinding.FragmentSubwayPlusBusBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.PathData
import com.leejinsil.keepthetime.utils.ODsayServerUtil
import org.json.JSONObject

class SubwayPlusBusFragment(
    val mAppointmentData: AppointmentData,
) : BaseFragment() {

    lateinit var binding : FragmentSubwayPlusBusBinding

    val mSubwayPlusBusList = ArrayList<PathData>()

    lateinit var mSubwayPlusBusAdapter : PathListRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subway_plus_bus, container, false)
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

        mSubwayPlusBusAdapter = PathListRecyclerViewAdapter(mContext, mSubwayPlusBusList, mAppointmentData)
        binding.subwayPlusBusRecyclerView.adapter = mSubwayPlusBusAdapter
        binding.subwayPlusBusRecyclerView.layoutManager = LinearLayoutManager(mContext)

        getSubwayPlusBusFromServer()

    }

    fun getSubwayPlusBusFromServer() {

        ODsayServerUtil.getRequestSearchPubTransPath(mContext, mAppointmentData, object : ODsayServerUtil.JsonResponseHandler{

            override fun onResponse(jsonObj: JSONObject) {

                mSubwayPlusBusList.clear()

                val pathArr = jsonObj.getJSONArray("path")
                val pathObjList = ArrayList<JSONObject>()
                pathObjList.clear()

                for (i in 0 until pathArr.length()) {
                    val pathObj = pathArr.getJSONObject(i)
                    pathObjList.add(pathObj)
                }

                for (pathObj in pathObjList) {

                    val pathType = pathObj.getInt("pathType")

                    if (pathType == 3) {

                        val pathData = PathData.getPathDataFromJson(pathObj)
                        mSubwayPlusBusList.add(pathData)
//                        Log.d("지하철+버스", pathObj.toString())
                    }

                }

                mSubwayPlusBusAdapter.notifyDataSetChanged()

            }

        })

    }

}