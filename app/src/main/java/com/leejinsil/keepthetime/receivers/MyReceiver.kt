package com.leejinsil.keepthetime.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.leejinsil.keepthetime.NotificationActivity
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.SplashActivity
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.receivers.ReceiverConst.Companion.CHANNEL_ID
import com.leejinsil.keepthetime.receivers.ReceiverConst.Companion.CHANNEL_NAME_APPOINTMENT
import com.leejinsil.keepthetime.receivers.ReceiverConst.Companion.NOTIFICATION_ID

class MyReceiver : BroadcastReceiver() {

    lateinit var notificationManager : NotificationManager

    lateinit var mAppointmentBundle : Bundle
    lateinit var mAppointmentData : AppointmentData
    lateinit var mCannelName : String
    lateinit var mCannelDescription : String
    lateinit var mNotificationTitle : String
    lateinit var mNotificationDescription : String

    override fun onReceive(context: Context?, intent: Intent?) {

        getIntent(intent)

        notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(mCannelName!!, mCannelDescription!!)
        deliverNotification(context, mNotificationTitle!!, mNotificationDescription!!, mCannelName)

    }

    fun getIntent(intent: Intent?){

        mCannelName = intent?.getStringExtra("cannel_name").toString()
        mCannelDescription = intent?.getStringExtra("cannel_description").toString()
        mNotificationTitle = intent?.getStringExtra("notification_title").toString()
        mNotificationDescription = intent?.getStringExtra("notification_description").toString()

        mAppointmentBundle = intent?.getBundleExtra("appointment_bundle")!! // Bondle 받은 후 > Serializable 형태로 받음
        mAppointmentData = mAppointmentBundle.getSerializable("appointment") as AppointmentData

    }

    fun createNotificationChannel(cannelName : String, cannelDesctiption : String){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {

            val notificationCannel = NotificationChannel(CHANNEL_ID, cannelName, NotificationManager.IMPORTANCE_HIGH)

            notificationCannel.enableLights(true) //불빛
            notificationCannel.lightColor = Color.RED // 색상
            notificationCannel.enableVibration(true) //진동여부
            notificationCannel.description = cannelDesctiption // 상세내용

            notificationManager.createNotificationChannel(notificationCannel) // 채널등록

        }

    }

    fun deliverNotification(context: Context, contentTitle : String, contentText : String, cannelName : String){

        var contentIntent : Intent

        if (cannelName == CHANNEL_NAME_APPOINTMENT){

            contentIntent = Intent(context, SplashActivity::class.java)
            contentIntent.putExtra("appointment", mAppointmentData)

        }
        else {
            contentIntent = Intent(context, NotificationActivity::class.java)
        }

        val contentPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.keepthetime_logo)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())

    }

}