package com.leejinsil.keepthetime

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.adapters.StartPlaceRecyclerAdapter
import com.leejinsil.keepthetime.databinding.ActivityStartPlaceBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.PlaceData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartPlaceActivity : BaseActivity() {

    lateinit var binding : ActivityStartPlaceBinding

    val mStartPlaceList = ArrayList<PlaceData>()

    lateinit var mStartPlaceAdapter : StartPlaceRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_place)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        mStartPlaceAdapter = StartPlaceRecyclerAdapter(mContext, mStartPlaceList)
        binding.startPlaceRecyclerView.adapter = mStartPlaceAdapter
        binding.startPlaceRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }

    fun getStartPlaceListFromServer(){

        apiList.getRequestPlaceList().enqueue( object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful) {

                    val br = response.body()!!

                    mStartPlaceList.clear()
                    mStartPlaceList.addAll(br.data.places)

                    mStartPlaceAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })

    }

    override fun onResume() {
        super.onResume()
        getStartPlaceListFromServer()
    }

}