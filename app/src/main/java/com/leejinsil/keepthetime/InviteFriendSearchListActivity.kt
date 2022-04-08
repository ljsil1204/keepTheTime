package com.leejinsil.keepthetime

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityInviteFriendSearchListBinding

class InviteFriendSearchListActivity : BaseActivity() {

    lateinit var binding : ActivityInviteFriendSearchListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite_friend_search_list)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}