package com.leejinsil.keepthetime

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.leejinsil.keepthetime.api.APIList
import com.leejinsil.keepthetime.api.ServerAPI

abstract class BasePopupActivity : Activity(){

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

}