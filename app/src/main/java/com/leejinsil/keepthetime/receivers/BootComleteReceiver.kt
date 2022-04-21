package com.leejinsil.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.leejinsil.keepthetime.api.APIList
import com.leejinsil.keepthetime.api.ServerAPI
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.receivers.MyReceiver
import com.leejinsil.keepthetime.receivers.ReceiverConst
import com.leejinsil.keepthetime.receivers.ReceiverConst.Companion.CHANNEL_DESCRIPTION_APPOINTMENT
import com.leejinsil.keepthetime.receivers.ReceiverConst.Companion.CHANNEL_NAME_APPOINTMENT
import com.leejinsil.keepthetime.utils.AppointmentAlarmContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BootComleteReceiver : BroadcastReceiver() {

    val mAppointmentList = ArrayList<AppointmentData>()

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == "android.intent.action.BOOT_COMPLETED"){

            Log.d("BootComlete", "부팅완료")

            getAppointmentFromServer(context!!)

            for (appointment in mAppointmentList) {

                if (AppointmentAlarmContextUtil.getAlarmCheck(context, appointment.id)){

                    val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    val receiverIntent = Intent(context, MyReceiver::class.java)
                    receiverIntent.putExtra("cannel_name", CHANNEL_NAME_APPOINTMENT)
                    receiverIntent.putExtra("cannel_description", CHANNEL_DESCRIPTION_APPOINTMENT)

                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        ReceiverConst.NOTIFICATION_ID,
                        receiverIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    )

                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        AppointmentAlarmContextUtil.getAlarmReservationTime(context, appointment.id),
                        pendingIntent
                    )

                }

            }

        }

    }


    fun getAppointmentFromServer(context: Context) {

        val retrofit = ServerAPI.getRetrofit(context)
        val apiList = retrofit.create(APIList::class.java)

        apiList.getRequestMyAppointmentList().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    val br = response.body()!!

                    mAppointmentList.clear()
                    mAppointmentList.addAll(br.data.appointments)

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

    }

}