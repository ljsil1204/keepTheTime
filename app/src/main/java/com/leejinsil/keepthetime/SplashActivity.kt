package com.leejinsil.keepthetime

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.leejinsil.keepthetime.utils.ContextUtil

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



//        2.5초 지난 후 실행
       val myHandler = Handler(Looper.getMainLooper())
        myHandler.postDelayed({

            val userAutoLogin = ContextUtil.getAutoLoginCheck(mContext)

            var myIntent : Intent

            if (userAutoLogin){
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