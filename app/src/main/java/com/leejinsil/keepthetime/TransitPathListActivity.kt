package com.leejinsil.keepthetime

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.leejinsil.keepthetime.adapters.MapDetailViewPager2Adapter
import com.leejinsil.keepthetime.databinding.ActivityViewMapDetailBinding
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.ResultData
import com.leejinsil.keepthetime.fragments.SubwayFragment
import com.leejinsil.keepthetime.utils.ODsayServerUtil
import org.json.JSONObject

class TransitPathListActivity : BaseActivity() {

    lateinit var binding : ActivityViewMapDetailBinding

    lateinit var mAppointmentData : AppointmentData

    var mPubTrafficData = ResultData()

//    lateinit var mPubTrafficData : ResultData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_map_detail)
        mAppointmentData = intent.getSerializableExtra("appointment") as AppointmentData
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        actionBarTitle.setText(R.string.actionbar_title_transit_path)

        getPubCount()

        binding.mapDetailViewPager2.adapter = MapDetailViewPager2Adapter(this, mAppointmentData)
        binding.mapDetailViewPager2.offscreenPageLimit = 3

        setTabLayout()
    }

    fun setTabLayout() {
        TabLayoutMediator(binding.mapDetailTabLayout, binding.mapDetailViewPager2, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when(position) {
                0 -> {
                    tab.text = "지하철 (${mPubTrafficData.subwayCount})"
//                    tab.icon = getDrawable(R.drawable.tablayout_icon_subway)
                }
                1 -> {
                    tab.text = "버스 (${mPubTrafficData.busCount})"
//                    tab.icon = getDrawable(R.drawable.tablayout_icon_bus)
                }
                else -> {
                    tab.text = "지하철 + 버스 (${mPubTrafficData.subwayBusCount})"
//                    tab.icon = getDrawable(R.drawable.tablayout_icon_walk)
                }
            }
        }).attach()
    }

    fun getPubCount() {

        ODsayServerUtil.getRequestSearchPubTransPath(mContext, mAppointmentData, object : ODsayServerUtil.JsonResponseHandler{
            override fun onResponse(jsonObj: JSONObject) {

                mPubTrafficData = ResultData.getResultDataFromJson(jsonObj)

                setTabLayout()

            }
        })

    }

}