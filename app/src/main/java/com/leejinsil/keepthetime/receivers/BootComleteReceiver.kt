package com.leejinsil.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.receivers.MyReceiver
import com.leejinsil.keepthetime.receivers.ReceiverConst
import com.leejinsil.keepthetime.receivers.ReceiverConst.Companion.CHANNEL_DESCRIPTION_APPOINTMENT
import com.leejinsil.keepthetime.receivers.ReceiverConst.Companion.CHANNEL_NAME_APPOINTMENT
import com.leejinsil.keepthetime.utils.ContextUtil

class BootComleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == "android.intent.action.BOOT_COMPLETED"){

            Log.d("BootComlete", "부팅완료")

//          AlarmManager 생성
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

//          PendingIntent 생성
            val receiverIntent = Intent(context, MyReceiver::class.java)
            receiverIntent.putExtra("cannel_name", CHANNEL_NAME_APPOINTMENT)
            receiverIntent.putExtra("cannel_description", CHANNEL_DESCRIPTION_APPOINTMENT)
            receiverIntent.putExtra("notification_title", ContextUtil.getAlarmTitle(context))
            receiverIntent.putExtra("notification_description", ContextUtil.getAlarmDescription(context))

            val PendingIntent = PendingIntent.getBroadcast(
                context,
                ReceiverConst.NOTIFICATION_ID,
                receiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                ContextUtil.getAlarmReservationTime(context),
                PendingIntent
            )

        }

    }

}