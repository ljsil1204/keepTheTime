package com.leejinsil.keepthetime

import android.content.Intent
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

    companion object{
        lateinit var flag : StartPlaceActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_place)
        flag = this
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnStartPlaceAdd.setOnClickListener {

            val myIntent = Intent(mContext, AddStartPlaceActivity::class.java)
            startActivity(myIntent)

        }

    }

    override fun setValues() {

        actionBarTitle.setText(R.string.actionbar_title_start_place_list)

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