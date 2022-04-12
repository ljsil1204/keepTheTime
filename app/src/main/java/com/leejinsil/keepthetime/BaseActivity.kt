package com.leejinsil.keepthetime

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.leejinsil.keepthetime.api.APIList
import com.leejinsil.keepthetime.api.ServerAPI
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.DataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseActivity : AppCompatActivity(){

    lateinit var mContext : Context

    lateinit var apiList : APIList

    lateinit var actionBarLayout : FrameLayout
    lateinit var actionBarBack : ImageView
    lateinit var actionBarTitle : TextView
    lateinit var actionBarNotification : FrameLayout
    lateinit var actionBarNotificationNum : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this

        val retrofit = ServerAPI.getRetrofit(mContext)
        apiList = retrofit.create(APIList::class.java)

        supportActionBar?.let {
            setCustomActionBar()
        }

    }

    abstract fun setupEvents()
    abstract fun setValues()

    fun setCustomActionBar(){

        val defaultActionBar = supportActionBar!!

        defaultActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        defaultActionBar.setCustomView(R.layout.my_custom_action_bar)

        val toolbar = defaultActionBar.customView.parent as Toolbar
        toolbar.setContentInsetsAbsolute(0,0)

        defaultActionBar.elevation = 0f

        actionBarLayout = defaultActionBar.customView.findViewById(R.id.actionBarLayout)
        actionBarBack = defaultActionBar.customView.findViewById(R.id.actionBarBack)
        actionBarTitle = defaultActionBar.customView.findViewById(R.id.actionBarTitle)
        actionBarNotification = defaultActionBar.customView.findViewById(R.id.actionBarNotification)
        actionBarNotificationNum = defaultActionBar.customView.findViewById(R.id.actionBarNotificationNum)

        actionBarBack.setOnClickListener {
            finish()
        }

        actionBarNotification.setOnClickListener {
            val myInetent = Intent(mContext, NotificationActivity::class.java)
            startActivity(myInetent)
        }

    }

}