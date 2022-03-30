package com.leejinsil.keepthetime.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.SearchUserActivity
import com.leejinsil.keepthetime.adapters.FriendListRecyclerAdapter
import com.leejinsil.keepthetime.databinding.FragmentFriendListBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendListFragment : BaseFragment() {

    lateinit var binding : FragmentFriendListBinding

    val mMyFriendsList = ArrayList<UserData>()

    lateinit var mFriendAdapter : FriendListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_list, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnFriendAdd.setOnClickListener {

            val myIntent = Intent(mContext, SearchUserActivity::class.java)
            startActivity(myIntent)

        }

    }

    override fun setValues() {

        mFriendAdapter = FriendListRecyclerAdapter(mContext, mMyFriendsList)
        binding.myFriendRecyclerView.adapter = mFriendAdapter
        binding.myFriendRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }

    fun getMyFriendsFromServer(){

        apiList.getrequestFriendList("my").enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    val br = response.body()!!

                    mMyFriendsList.clear()

                    mMyFriendsList.addAll(br.data.friends)

                    mFriendAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

    }

    override fun onResume() {
        super.onResume()
        getMyFriendsFromServer()
    }

}