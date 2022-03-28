package com.leejinsil.keepthetime.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.ViewMapGuideActivity
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.InfoData
import com.leejinsil.keepthetime.datas.PathData
import com.leejinsil.keepthetime.datas.SubPathData
import com.odsay.odsayandroidsdk.ODsayData

class PathListRecyclerViewAdapter(
    val mContext : Context,
    val mList : List<SubPathData>,
    val mAppointmentData : AppointmentData,
) : RecyclerView.Adapter<PathListRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)  {

        val txtTotalTime = view.findViewById<TextView>(R.id.txtTotalTime)
        val txtTotalDistance = view.findViewById<TextView>(R.id.txtTotalDistance)
        val txtTrafficCode = view.findViewById<TextView>(R.id.txtTrafficCode)
        val txtStartName = view.findViewById<TextView>(R.id.txtStartName)
        val txtEndName = view.findViewById<TextView>(R.id.txtEndName)

        fun bind (data: SubPathData) {

            txtTotalTime.text = data.sectionTime.toString()
            txtTotalDistance.text = "${data.distance / 1000}km"
            txtStartName.text = data.startName
            txtEndName.text = data.endName

            when (data.trafficType){

                1 -> {
                    val txtSubwayTrafficCode = data.laneSubwayList[0].name
                    txtTrafficCode.text = txtSubwayTrafficCode.replace("수도권 ", "")
                }

            }


            itemView.setOnClickListener {

                val myIntent = Intent(mContext, ViewMapGuideActivity::class.java)
                myIntent.putExtra("sub_path", data)
                myIntent.putExtra("appointment", mAppointmentData)
                mContext.startActivity(myIntent)

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val row = LayoutInflater.from(mContext).inflate(R.layout.path_list_item, parent, false)
        return MyViewHolder(row)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mList[position]
        holder.bind(data)

    }

    override fun getItemCount(): Int = mList.size

}