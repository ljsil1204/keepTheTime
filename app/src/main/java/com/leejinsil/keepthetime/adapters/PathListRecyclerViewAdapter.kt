package com.leejinsil.keepthetime.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.ViewMapGuideActivity
import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.PathData
import java.text.DecimalFormat

class PathListRecyclerViewAdapter(
    val mContext : Context,
    val mList : List<PathData>,
    val mAppointmentData : AppointmentData,
) : RecyclerView.Adapter<PathListRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)  {

        val txtTotalTime = view.findViewById<TextView>(R.id.txtTotalTime)
        val txtTotalDistance = view.findViewById<TextView>(R.id.txtTotalDistance)
        val txtPayment = view.findViewById<TextView>(R.id.txtPayment)
        val txtTrafficCode = view.findViewById<TextView>(R.id.txtTrafficCode)
        val txtStartName = view.findViewById<TextView>(R.id.txtStartName)
        val txtEndName = view.findViewById<TextView>(R.id.txtEndName)

        fun bind (data: PathData) {

            txtTotalTime.text = data.info.totalTime.toString()
            txtTotalDistance.text = "${String.format("%.1f", data.info.totalDistance / 1000)}km"
            txtPayment.text = "${DecimalFormat("#,###").format(data.info.payment)}원"

            for (subPathObj in data.subPathList) {

                if (data.info.firstStartStation == subPathObj.startName){

                    when(data.pathType) {

                        1 -> {
                            txtTrafficCode.text = subPathObj.laneSubwayList[0].name.replace("수도권 ", "")
                        }
                        2 -> {
                            txtTrafficCode.text = "${subPathObj.laneBusList[0].busNo}번"
                        }
                        3 -> {
                            if (subPathObj.trafficType == 1) {
                                txtTrafficCode.text = subPathObj.laneSubwayList[0].name.replace("수도권 ", "")
                            }

                            if (subPathObj.trafficType == 2) {
                                txtTrafficCode.text = "${subPathObj.laneBusList[0].busNo}번"
                            }
                        }

                    }

                }

            }


            txtStartName.text = "${data.info.firstStartStation}역"
            txtEndName.text = "${data.info.lastEndStation}역"

            itemView.setOnClickListener {

                val myIntent = Intent(mContext, ViewMapGuideActivity::class.java)
                myIntent.putExtra("path", data)
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