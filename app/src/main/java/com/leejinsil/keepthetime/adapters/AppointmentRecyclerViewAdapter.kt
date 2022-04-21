package com.leejinsil.keepthetime.adapters

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.ViewAppointmentDetailActivity
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.receivers.MyReceiver
import com.leejinsil.keepthetime.receivers.ReceiverConst
import com.leejinsil.keepthetime.utils.AppointmentAlarmContextUtil
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class AppointmentRecyclerViewAdapter(
    val mContext : Context,
    val mList : List<AppointmentData>,
) : RecyclerView.Adapter<AppointmentRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)  {

        val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
        val txtCreatAt = view.findViewById<TextView>(R.id.txtCreatAt)
        val txtDate = view.findViewById<TextView>(R.id.txtDate)
        val txtHour = view.findViewById<TextView>(R.id.txtHour)
        val switchAlarm = view.findViewById<Switch>(R.id.switchAlarm)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind (data: AppointmentData) {

            txtTitle.text = data.title

            val sdf = SimpleDateFormat("yy년 M월 d일 a h시 mm분")
            val sdfDay = SimpleDateFormat("yy년 M월 d일")
            val sdfHour = SimpleDateFormat("a h:mm")
            val sdfBasic = SimpleDateFormat("yyyy-MM-dd")

            val localCal = Calendar.getInstance()
            localCal.time = data.created_at

            val localTimeZone = localCal.timeZone
            val diffHour = localTimeZone.rawOffset / 60 / 60 / 1000

            localCal.add(Calendar.HOUR, diffHour)

            val localCalBasicSdf = sdfBasic.format(localCal.time)
            val today = LocalDate.now()

            if (localCalBasicSdf.toString() == today.toString()) {
                txtCreatAt.text = sdfHour.format(localCal.time)
            }
            else {
                txtCreatAt.text = sdfDay.format(localCal.time)
            }

            txtDate.text = sdfDay.format(data.datetime)
            txtHour.text = sdfHour.format(data.datetime)

            switchAlarm.isChecked = AppointmentAlarmContextUtil.getAlarmCheck(mContext, data.id)

            switchAlarm.setOnCheckedChangeListener { compoundButton, isChecked ->

                AppointmentAlarmContextUtil.setAlarmCheck(mContext, data.id, isChecked)

                setAlarm(data, mContext, isChecked)

            }

            itemView.setOnClickListener {

                val myIntent = Intent(mContext, ViewAppointmentDetailActivity::class.java)
                myIntent.putExtra("appointment", data)
                mContext.startActivity(myIntent)

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val row = LayoutInflater.from(mContext).inflate(R.layout.appointment_list_item, parent, false)
        return MyViewHolder(row)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mList[position]
        holder.bind(data)

    }

    override fun getItemCount(): Int = mList.size

    fun setAlarm(data: AppointmentData, context: Context, check : Boolean) {

//        AlarmManager 생성
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

//        receiverIntent 생성
        val receiverIntent = Intent(mContext, MyReceiver::class.java)
        receiverIntent.putExtra("appointment", data)
        receiverIntent.putExtra("cannel_name", ReceiverConst.CHANNEL_NAME_APPOINTMENT)
        receiverIntent.putExtra("cannel_description", ReceiverConst.CHANNEL_DESCRIPTION_APPOINTMENT)

//        PendingIntent 생성
        val pendingIntent = PendingIntent.getBroadcast(
            mContext,
            ReceiverConst.NOTIFICATION_ID,
            receiverIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val alarmTime = Calendar.getInstance()
        alarmTime.timeInMillis = AppointmentAlarmContextUtil.getAlarmReservationTime(mContext, data.id)

        val sdf = SimpleDateFormat("yy년 MM월 dd일 E요일 a h:mm")
        val alarmTimeFormat = sdf.format(alarmTime.time)

        if (check) {

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                alarmTime.timeInMillis,
                pendingIntent
            )

            Toast.makeText(mContext, "${alarmTimeFormat}에 알림이 울립니다.", Toast.LENGTH_SHORT).show()
        }
        else {

            alarmManager.cancel(pendingIntent)
            Toast.makeText(mContext, "알람예약이 취소되었습니다.", Toast.LENGTH_SHORT).show()

        }

    }

}