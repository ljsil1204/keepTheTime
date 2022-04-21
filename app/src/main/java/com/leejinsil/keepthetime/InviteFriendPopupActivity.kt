package com.leejinsil.keepthetime

import android.os.Bundle
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityInviteFriendPopupBinding

class InviteFriendPopupActivity : BasePopupActivity() {

    lateinit var binding : ActivityInviteFriendPopupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite_friend_popup)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.popupClose.setOnClickListener {
            finish()
        }

    }

    override fun setValues() {

    }
}