package com.leejinsil.keepthetime.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.datas.PlaceData

class StartPlaceRecyclerAdapter(
    val mContext : Context,
    val mList : List<PlaceData>
) : RecyclerView.Adapter<StartPlaceRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
        val iconPrimary = view.findViewById<TextView>(R.id.iconPrimary)
        val btnPopupMenu = view.findViewById<ImageView>(R.id.btnPopupMenu)

        fun bind(data: PlaceData) {

            txtTitle.text = data.name

            if (data.isPrimary) {

                iconPrimary.visibility = View.VISIBLE

            }

            btnPopupMenu.setOnClickListener {

                val popup = PopupMenu(mContext, btnPopupMenu)

                popup.menuInflater.inflate(R.menu.popupmenu_for_start_place, popup.menu)

                popup.show()

            }

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