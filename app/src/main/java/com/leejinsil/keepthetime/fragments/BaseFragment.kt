package com.leejinsil.keepthetime.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.leejinsil.keepthetime.api.APIList
import com.leejinsil.keepthetime.api.ServerAPI

abstract class BaseFragment : Fragment() {

    lateinit var mContext : Context

    lateinit var apiList : APIList

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mContext = requireContext()

        val retrofit = ServerAPI.getRetrofit(mContext)
        apiList = retrofit.create(APIList::class.java)

    }


    abstract fun setupEvents()
    abstract fun setValues()

}