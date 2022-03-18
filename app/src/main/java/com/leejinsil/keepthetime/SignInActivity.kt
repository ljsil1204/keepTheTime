package com.leejinsil.keepthetime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivitySignInBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.utils.ContextUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : BaseActivity() {
    lateinit var binding : ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        회원가입 이벤트
        binding.btnSingUp.setOnClickListener {

            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)

        }

//        로그인 이벤트
       binding.btnSignIn.setOnClickListener {

           val inputEmail = binding.edtEmail.text.toString()
           val inputPassword = binding.edtPassword.text.toString()

            apiList.postRequestLogin(inputEmail, inputPassword).enqueue( object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

                    if (response.isSuccessful){
                        val br = response.body()!!
                        Toast.makeText(mContext, "${br.data.user.nick_name}님, 로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show()

                        ContextUtil.setUserToken(mContext, br.data.token) // 로그인 성공 시 토큰 값 저장

                        val myIntent = Intent(mContext, MainActivity::class.java)
                        startActivity(myIntent)

                        finish()

                    }
                    else {
                        val br = response.errorBody()!!.string()
                        val jsonObj = JSONObject(br)
                        val message = jsonObj.getString("message")
                        Log.d("로그인 실패", jsonObj.toString())
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }
                    
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            } )

        }

    }

    override fun setValues() {

    }
}