package com.leejinsil.keepthetime

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivitySignUpBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : BaseActivity() {

    lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnSingUp.setOnClickListener {

            val inputEmail = binding.edtEmail.text.toString()
            val inputPassword = binding.edtPassword.text.toString()
            val inputNickname = binding.edtNickname.text.toString()

            apiList.putRequestSignUp(inputEmail, inputPassword, inputNickname).enqueue( object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    
                    if (response.isSuccessful){
                        
                        val br = response.body()!!
                        Toast.makeText(mContext, "회원가입에 성공하였습니다. ${br.data.user.nick_name}님, 가입을 축하합니다!", Toast.LENGTH_SHORT).show()
                        finish()
                        
                    }
                    
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                
                }

            })

        }

    }

    override fun setValues() {

    }
}