package com.leejinsil.keepthetime

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.adapters.ArrivalFriendRecyclerAdapter
import com.leejinsil.keepthetime.adapters.InviteFriendPopupRecyclerAdapter
import com.leejinsil.keepthetime.databinding.ActivityInviteFriendPopupBinding
import com.leejinsil.keepthetime.datas.UserData

class InviteFriendPopupActivity : BasePopupActivity() {

    lateinit var binding : ActivityInviteFriendPopupBinding

    lateinit var mInviteSelectedFriendList : ArrayList<UserData>

    lateinit var mInviteFriendAdapter : InviteFriendPopupRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite_friend_popup)
        mInviteSelectedFriendList = intent.getParcelableArrayListExtra<UserData>("invite_selected")!!
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.popupClose.setOnClickListener {
            finish()
        }

    }

    override fun setValues() {

        mInviteFriendAdapter = InviteFriendPopupRecyclerAdapter(mContext, mInviteSelectedFriendList)
        binding.inviteFriendRecycleView.adapter = mInviteFriendAdapter
        binding.inviteFriendRecycleView.layoutManager = LinearLayoutManager(mContext)

    }
}