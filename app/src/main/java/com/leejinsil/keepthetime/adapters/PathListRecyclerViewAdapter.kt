package com.leejinsil.keepthetime.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leejinsil.keepthetime.R
import com.odsay.odsayandroidsdk.ODsayData

class PathListRecyclerViewAdapter(
    val mContext : Context,
    val mList : List<ODsayData>,
) : RecyclerView.Adapter<PathListRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)  {

        fun bind (data: ODsayData) {

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