package com.leejinsil.keepthetime.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.datas.AppointmentData
import java.text.SimpleDateFormat

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

        fun bind (data: AppointmentData) {

            txtTitle.text = data.title

            val sdf = SimpleDateFormat("yy년 M월 m일 a h시 m분")
            txtCreatAt.text = sdf.format(data.created_at)
            txtDateTime.text = sdf.format(data.datetime)
            txtStartPlace.text = data.start_place
            txtFinishPlace.text = data.place

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val row = LayoutInflater.from(mContext).inflate(R.layout.appointment_list_item, parent, false)
        return MyViewHolder(row)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mList[position]
        holder.bind(data)

    }

    override fun getItemCount(): Int = mList.size

}