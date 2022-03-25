package com.leejinsil.keepthetime

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.leejinsil.keepthetime.api.APIList
import com.leejinsil.keepthetime.api.ServerAPI
import com.leejinsil.keepthetime.datas.AppointmentData
import java.io.Serializable

abstract class BaseActivity : AppCompatActivity(){

    lateinit var mContext : Context

    lateinit var apiList : APIList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this

        val retrofit = ServerAPI.getRetrofit(mContext)
        apiList = retrofit.create(APIList::class.java)
    }

    abstract fun setupEvents()
    abstract fun setValues()


//        프래그먼트로 데이터 넘기는 함수
    fun fragmentDataArguments(key : String , data : AppointmentData, fmName : Fragment) = fmName.apply {
        arguments = Bundle().apply {
            putSerializable(key, data)
        }
    }

}