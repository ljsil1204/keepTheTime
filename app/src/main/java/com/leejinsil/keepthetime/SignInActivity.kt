package com.leejinsil.keepthetime

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivitySignInBinding

class SignInActivity : BaseActivity() {
    lateinit var binding : ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        로그인
       binding.btnSignIn.setOnClickListener {

           val inputEmail = binding.edtEmail.text.toString()
           val inputPassword = binding.edtPassword.text.toString()

        }

    }

    override fun setValues() {

    }
}