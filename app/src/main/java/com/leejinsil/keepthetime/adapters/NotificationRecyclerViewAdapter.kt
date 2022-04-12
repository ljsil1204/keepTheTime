package com.leejinsil.keepthetime.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.datas.NotificationData
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class NotificationRecyclerViewAdapter(
    val mContext : Context,
    val mList : List<NotificationData>,
) : RecyclerView.Adapter<NotificationRecyclerViewAdapter.MyViewHolder>() {

    private lateinit var itemClickListener : ItemClickListener

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)  {

        val imgNotification = view.findViewById<ImageView>(R.id.imgNotification)
        val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
        val txtMessage = view.findViewById<TextView>(R.id.txtMessage)
        val txtDate = view.findViewById<TextView>(R.id.txtDate)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind (data: NotificationData) {

            when (data.type){
                "친구추가요청" -> Glide.with(mContext).load(R.drawable.notification_icon_friend).into(imgNotification)
                "약속초대" -> Glide.with(mContext).load(R.drawable.notification_icon_appointment).into(imgNotification)
            }

            txtTitle.text = data.title
            txtMessage.text = data.message

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
                txtDate.text = sdfHour.format(localCal.time)
            }
            else {
                txtDate.text = sdfDay.format(localCal.time)
            }

            if (data.is_read) {
                itemView.alpha = 0.5f
            }

            itemView.setOnClickListener {
                itemClickListener.onClick(it, position)
                itemClickListener.onClickDataType(it, position, data.type)

            }

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

    interface ItemClickListener{
        fun onClick(view: View, position: Int)
        fun onClickDataType(view: View, position: Int, type : String)
    }

    fun setItemClickListener (itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}