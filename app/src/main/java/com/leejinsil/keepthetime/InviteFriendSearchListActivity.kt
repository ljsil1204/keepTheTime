package com.leejinsil.keepthetime

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.adapters.InviteFriendSearchRecyclerAdapter
import com.leejinsil.keepthetime.databinding.ActivityInviteFriendSearchListBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InviteFriendSearchListActivity : BaseActivity() {

    lateinit var binding : ActivityInviteFriendSearchListBinding

    val mMyFriendSearchList = ArrayList<UserData>()

    lateinit var mFriendAdapter : InviteFriendSearchRecyclerAdapter

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

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }

    override fun setValues() {

        mFriendAdapter = InviteFriendSearchRecyclerAdapter(mContext, mMyFriendSearchList)
        binding.myFriendRecyclerView.adapter = mFriendAdapter
        binding.myFriendRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }


    fun getMyFriendsFromServer(){

        apiList.getrequestFriendList("my").enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    val br = response.body()!!

                    mMyFriendSearchList.clear()

                    mMyFriendSearchList.addAll(br.data.friends)

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