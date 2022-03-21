package com.leejinsil.keepthetime.adapters

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.datas.AppointmentData
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class AppointmentRecyclerViewAdapter(
    val mContext : Context,
    val mList : List<AppointmentData>,
) : RecyclerView.Adapter<AppointmentRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)  {

        val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
        val txtCreatAt = view.findViewById<TextView>(R.id.txtCreatAt)
        val txtDateTime = view.findViewById<TextView>(R.id.txtDateTime)
        val txtStartPlace = view.findViewById<TextView>(R.id.txtStartPlace)
        val txtFinishPlace = view.findViewById<TextView>(R.id.txtFinishPlace)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind (data: AppointmentData) {

            txtTitle.text = data.title

            val sdf = SimpleDateFormat("yy년 M월 d일 a h시 mm분")
            val sdfDay = SimpleDateFormat("yy년 M월 d일")
            val sdfHour = SimpleDateFormat("a h시 m분")
            val sdfBasic = SimpleDateFormat("yyyy-MM-dd")

            val localCal = Calendar.getInstance()
            localCal.time = data.created_at

            val localTimeZone = localCal.timeZone
            val diffHour = localTimeZone.rawOffset / 60 / 60 / 1000

            localCal.add(Calendar.HOUR, diffHour)

            val localCalSdf = sdfBasic.format(localCal.time)
            val today = LocalDate.now()

            if (localCalSdf.toString() == today.toString()) {
                txtCreatAt.text = sdfHour.format(localCal.time)
            }
            else {
                txtCreatAt.text = sdfDay.format(localCal.time)
            }

            txtDateTime.text = sdf.format(data.datetime)

            txtStartPlace.text = data.start_place
            txtFinishPlace.text = data.place

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

}