package com.leejinsil.keepthetime

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        val myHandler = Handler(Looper.getMainLooper())
        myHandler.postDelayed({

            val myIntent = Intent(mContext, SignInActivity::class.java)
            startActivity(myIntent)

            finish()

        }, 2500)

    }

    override fun setValues() {
    }
}