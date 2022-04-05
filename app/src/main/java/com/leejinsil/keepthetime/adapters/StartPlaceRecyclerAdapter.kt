package com.leejinsil.keepthetime.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.api.APIList
import com.leejinsil.keepthetime.api.ServerAPI
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.PlaceData
import com.leejinsil.keepthetime.datas.UserData
import com.leejinsil.keepthetime.fragments.FriendListFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartPlaceRecyclerAdapter(
    val mContext : Context,
    val mList : List<PlaceData>
) : RecyclerView.Adapter<StartPlaceRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        fun bind(data: PlaceData) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.start_place_list_item, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mList[position]
        holder.bind(data)

    }

    override fun getItemCount() = mList.size

}