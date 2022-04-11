package com.leejinsil.keepthetime

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityFindPasswordBinding

class FindPasswordActivity : BaseActivity() {

    lateinit var binding: ActivityFindPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_password)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnFindPw.setOnClickListener {
            postFindPwToSever()
        }

    }

    override fun setValues() {

    }

    fun postFindPwToSever() {

        val inputId = binding.edtId.text.toString()
        val inputNickname = binding.edtNickname.text.toString()

    }

}