package com.leejinsil.keepthetime

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityMainBinding
import com.leejinsil.keepthetime.utils.ContextUtil

class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnLogOut.setOnClickListener {

            val alert = AlertDialog.Builder(mContext)
                .setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

                    ContextUtil.setUserToken(mContext, "")
                    ContextUtil.setAutoLoginCheck(mContext, false)

                    val myIntent = Intent(mContext, SplashActivity::class.java)
                    startActivity(myIntent)
                    finish()

                })
                .setNegativeButton("취소", null)
                .show()

        }

    }

    override fun setValues() {

    }
}