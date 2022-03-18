package com.leejinsil.keepthetime

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        2.5초 전에 토큰 유효성 검사
        var isTokenOk = false

        apiList.getRequestMyInfo().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                isTokenOk = response.isSuccessful
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })


//        2.5초 지난 후 실행
       val myHandler = Handler(Looper.getMainLooper())
        myHandler.postDelayed({

            val userAutoLogin = ContextUtil.getAutoLoginCheck(mContext)
            Log.d("체크여부", userAutoLogin.toString())

            var myIntent : Intent

            if (userAutoLogin && isTokenOk){
                myIntent = Intent(mContext, MainActivity::class.java)
            }
            else {
                myIntent = Intent(mContext, SignInActivity::class.java)
            }

            startActivity(myIntent)
            finish()

        }, 2500)

    }

    override fun setValues() {
    }
}