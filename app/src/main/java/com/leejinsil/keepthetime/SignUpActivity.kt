package com.leejinsil.keepthetime

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivitySignUpBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : BaseActivity() {

    lateinit var binding : ActivitySignUpBinding

    var isEmailDuplOk = false
    var isNicknameDuplOk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        이메일 editText가 변경될 때, 재검사 문구 출력
        binding.edtEmail.addTextChangedListener {
            binding.txtEmailCheck.visibility = View.VISIBLE
            binding.txtEmailCheck.text = "이메일 중복 검사를 진행해주세요."
        }

//        이메일 중복체크
        binding.btnEmailCheck.setOnClickListener {

            val inputEmail = binding.edtEmail.text.toString()

            apiList.getRequestDuplicatedCheck("EMAIL", inputEmail).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

                    binding.txtEmailCheck.visibility = View.VISIBLE

                    if (response.isSuccessful){
                        val br = response.body()!!
                        binding.txtEmailCheck.text = br.message
                        isEmailDuplOk = true
                    }
                    else {
                        val br = response.errorBody()!!.string()
                        val jsonObj = JSONObject(br)
                        val message = jsonObj.getString("message")
                        binding.txtEmailCheck.text = message
                        isEmailDuplOk = false
                    }

                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })
        }

//        닉네임 editText가 변경될 때, 재검사 문구 출력
        binding.edtNickname.addTextChangedListener {
            binding.txtNicknameCheck.visibility = View.VISIBLE
            binding.txtNicknameCheck.text = "닉네임 중복 검사를 진행해주세요."
        }

//        닉네임 중복체크
        binding.btnNicknameCheck.setOnClickListener {

            val inputNickname = binding.edtNickname.text.toString()

            apiList.getRequestDuplicatedCheck("NICK_NAME", inputNickname).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

                    binding.txtNicknameCheck.visibility = View.VISIBLE

                    if (response.isSuccessful){
                        val br = response.body()!!
                        binding.txtNicknameCheck.text = br.message
                        isNicknameDuplOk = true
                    }
                    else {
                        val br = response.errorBody()!!.string()
                        val jsonObj = JSONObject(br)
                        val message = jsonObj.getString("message")
                        binding.txtNicknameCheck.text = message
                        isNicknameDuplOk = false
                    }

                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })
        }

//        회원가입 클릭 이벤트
        binding.btnSingUp.setOnClickListener {

            if (!isEmailDuplOk){
                Toast.makeText(mContext, "이메일 중복검사를 통과해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isNicknameDuplOk) {
                Toast.makeText(mContext, "닉네임 중복검사를 통과해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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
                    else {
                        val br = response.errorBody()!!.string()
                        val jsonObj = JSONObject(br)
                        val message = jsonObj.getString("message")
                        Toast.makeText(mContext, "회원가입에 실패하였습니다. ${message}", Toast.LENGTH_SHORT).show()
                    }
                    
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                
                }

            })

        }

    }

    override fun setValues() {
        actionBarTitle.setText(R.string.actionbar_title_sign_up)
    }



}