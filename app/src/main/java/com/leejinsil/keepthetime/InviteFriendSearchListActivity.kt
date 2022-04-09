package com.leejinsil.keepthetime

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.adapters.InviteSearchFriendRecyclerAdapter
import com.leejinsil.keepthetime.adapters.InviteSelectedFriendRecyclerAdapter
import com.leejinsil.keepthetime.databinding.ActivityInviteFriendSearchListBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InviteFriendSearchListActivity : BaseActivity() {

    lateinit var binding : ActivityInviteFriendSearchListBinding

    val mSearchFriendList = ArrayList<UserData>()

    lateinit var mSearchFriendAdapter : InviteSearchFriendRecyclerAdapter

    val mSelectedFriendList = ArrayList<UserData>()

    lateinit var mSelectedFriendAdapter : InviteSelectedFriendRecyclerAdapter

    lateinit var checkBoxFriend : CheckBox

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

                mSearchFriendAdapter.filter.filter(charSequence)
                mSelectedFriendList.clear()

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }

    override fun setValues() {

        getMyFriendsFromServer()

        mSearchFriendAdapter = InviteSearchFriendRecyclerAdapter(mContext, mSearchFriendList)
        binding.myFriendRecyclerView.adapter = mSearchFriendAdapter
        binding.myFriendRecyclerView.layoutManager = LinearLayoutManager(mContext)

        getSelectedFriendList()

        mSelectedFriendAdapter = InviteSelectedFriendRecyclerAdapter(mContext, mSelectedFriendList)
        binding.selectedInviteRecyclerView.adapter = mSelectedFriendAdapter
        binding.selectedInviteRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

        mSelectedFriendAdapter.setItemClickListener( object : InviteSelectedFriendRecyclerAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {

                val imgRemove = view.findViewById<ImageView>(R.id.imgRemove)

                imgRemove.setOnClickListener {
                    mSelectedFriendAdapter.removeItem(position)
                }

            }
        })

    }


    fun getMyFriendsFromServer(){

        apiList.getrequestFriendList("my").enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    val br = response.body()!!

                    mSearchFriendList.clear()

                    mSearchFriendList.addAll(br.data.friends)

                    mSearchFriendAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

    }

    fun getSelectedFriendList() {

        mSearchFriendAdapter.setItemClickListener( object : InviteSearchFriendRecyclerAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {

//                체크박스
                checkBoxFriend = view.findViewById<CheckBox>(R.id.checkBoxFriend)

                when(checkBoxFriend.isChecked) {
                    true -> {checkBoxFriend.isChecked = false}
                    false -> {checkBoxFriend.isChecked = true}
                }

//                선택된 친구 목록 데이터 추가
                val data = mSearchFriendList[position]

                if (!mSelectedFriendList.contains(data)){
                    mSelectedFriendList.add(data)
                    mSelectedFriendAdapter.notifyDataSetChanged()
                }
                else {
                    mSelectedFriendList.remove(data)
                    mSelectedFriendAdapter.notifyDataSetChanged()

                }

            }
        })

    }

}