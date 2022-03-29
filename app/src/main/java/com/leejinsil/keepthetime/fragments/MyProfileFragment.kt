package com.leejinsil.keepthetime.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.SplashActivity
import com.leejinsil.keepthetime.databinding.FragmentMyProfileBinding
import com.leejinsil.keepthetime.utils.ContextUtil

class MyProfileFragment : BaseFragment() {

    lateinit var binding : FragmentMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
                    myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // 프래그먼트 화면 종료
                    startActivity(myIntent)

                })
                .setNegativeButton("취소", null)
                .show()

        }

    }

    override fun setValues() {

    }


}