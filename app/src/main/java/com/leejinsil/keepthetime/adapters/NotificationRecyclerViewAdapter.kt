package com.leejinsil.keepthetime.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.ViewAppointmentDetailActivity
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.NotificationData
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class NotificationRecyclerViewAdapter(
    val mContext : Context,
    val mList : List<NotificationData>,
) : RecyclerView.Adapter<NotificationRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)  {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind (data: NotificationData) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val row = LayoutInflater.from(mContext).inflate(R.layout.notification_list_item, parent, false)
        return MyViewHolder(row)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mList[position]
        holder.bind(data)

    }

    override fun getItemCount(): Int = mList.size

}