package com.leejinsil.keepthetime

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.adapters.InviteFriendSearchRecyclerAdapter
import com.leejinsil.keepthetime.adapters.InviteFriendSelectedRecyclerAdapter
import com.leejinsil.keepthetime.databinding.ActivityInviteFriendSearchListBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InviteFriendSearchListActivity : BaseActivity() {

    lateinit var binding : ActivityInviteFriendSearchListBinding

    val mMyFriendSearchList = ArrayList<UserData>()

    lateinit var mFriendSearchAdapter : InviteFriendSearchRecyclerAdapter

    val mSelectedFriendList = ArrayList<UserData>()

    lateinit var mSelectedFriendAdapter : InviteFriendSelectedRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite_friend_search_list)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.edtNickname.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {

                mFriendSearchAdapter.filter.filter(charSequence)

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }

    override fun setValues() {

        getMyFriendsFromServer()

        mFriendSearchAdapter = InviteFriendSearchRecyclerAdapter(mContext, mMyFriendSearchList)
        binding.myFriendRecyclerView.adapter = mFriendSearchAdapter
        binding.myFriendRecyclerView.layoutManager = LinearLayoutManager(mContext)

        mFriendSearchAdapter.setItemClickListener( object : InviteFriendSearchRecyclerAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {

                val data = mMyFriendSearchList[position]

                Log.d("데이터", data.nick_name)

                if (!mSelectedFriendList.contains(data)){
                    mSelectedFriendList.add(data)
                    mSelectedFriendAdapter.notifyDataSetChanged()
                }

            }
        })

        mSelectedFriendAdapter = InviteFriendSelectedRecyclerAdapter(mContext, mSelectedFriendList)
        binding.selectedInviteRecyclerView.adapter = mSelectedFriendAdapter
        binding.selectedInviteRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

    }


    fun getMyFriendsFromServer(){

        apiList.getrequestFriendList("my").enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    val br = response.body()!!

                    mMyFriendSearchList.clear()

                    mMyFriendSearchList.addAll(br.data.friends)

                    mFriendSearchAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

    }


}