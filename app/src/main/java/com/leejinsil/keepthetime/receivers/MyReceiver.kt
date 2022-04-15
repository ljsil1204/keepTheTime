package com.leejinsil.keepthetime.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.receivers.ReceiverConst.Companion.CHANNEL_ID
import com.leejinsil.keepthetime.receivers.ReceiverConst.Companion.NOTIFICATION_ID

class MyReceiver : BroadcastReceiver() {

    lateinit var notificationManager : NotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {

        val cannelName = intent?.getStringExtra("cannel_name")
        val cannelDescription = intent?.getStringExtra("cannel_description")
        val notificationTitle = intent?.getStringExtra("notification_title")
        val notificationDescription = intent?.getStringExtra("notification_description")

        notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(cannelName!!, cannelDescription!!)
        deliverNotification(context, notificationTitle!!, notificationDescription!!)

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

    fun deliverNotification(context: Context, contentTitle : String, contentText : String){

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.keepthetime_logo)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())

    }

}