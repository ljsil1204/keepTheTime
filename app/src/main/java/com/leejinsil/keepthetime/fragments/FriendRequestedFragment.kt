package com.leejinsil.keepthetime.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.adapters.FriendRequestedRecyclerAdapter
import com.leejinsil.keepthetime.databinding.FragmentFriendRequestedBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendRequestedFragment : BaseFragment() {

    lateinit var binding : FragmentFriendRequestedBinding

    val mFriendRequestList = ArrayList<UserData>()

    lateinit var mFriendRequsetAdapter : FriendRequestedRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_requested, container, false)
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

        mFriendRequsetAdapter = FriendRequestedRecyclerAdapter(mContext, mFriendRequestList)
        binding.FriendRequestedRecyclerView.adapter = mFriendRequsetAdapter
        binding.FriendRequestedRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }

    fun getFriendRequestedFromServer() {

        apiList.getrequestFriendList("requested").enqueue( object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful) {

                    val br = response.body()!!

                    mFriendRequestList.clear()
                    mFriendRequestList.addAll(br.data.friends)
                    mFriendRequsetAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })

    }

    override fun onResume() {
        super.onResume()
        getFriendRequestedFromServer()
    }

}