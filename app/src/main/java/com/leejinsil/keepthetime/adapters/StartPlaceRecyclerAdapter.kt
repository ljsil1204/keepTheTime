package com.leejinsil.keepthetime.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.StartPlaceActivity
import com.leejinsil.keepthetime.api.APIList
import com.leejinsil.keepthetime.api.ServerAPI
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.PlaceData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

                val retrofit = ServerAPI.getRetrofit(mContext)
                val apiList = retrofit.create(APIList::class.java)

                val popup = PopupMenu(mContext, btnPopupMenu)

                popup.menuInflater.inflate(R.menu.popupmenu_for_start_place, popup.menu)

                popup.show()

                popup.setOnMenuItemClickListener {

                    when(it.itemId) {
                        R.id.actionDefaultPlaceChange -> {

                            apiList.patchRequestEditDefaultPlace(data.id).enqueue( object : Callback<BasicResponse>{
                                override fun onResponse(
                                    call: Call<BasicResponse>,
                                    response: Response<BasicResponse>
                                ) {

                                    if (response.isSuccessful) {

                                        StartPlaceActivity.flag.getStartPlaceListFromServer()
                                        Toast.makeText(mContext, "기본출발지가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                                        
                                    }
                                    
                                }

                                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                                }
                            })

                        }
                        R.id.actionDelet -> {

                            apiList.deleteRequestPlace(data.id).enqueue( object : Callback<BasicResponse>{
                                override fun onResponse(
                                    call: Call<BasicResponse>,
                                    response: Response<BasicResponse>
                                ) {

                                    if (response.isSuccessful) {

                                        StartPlaceActivity.flag.getStartPlaceListFromServer()
                                        Toast.makeText(mContext, "출발지가 삭제되었습니다.", Toast.LENGTH_SHORT).show()

                                    }

                                }

                                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                                }
                            })

                        }
                    }

                    return@setOnMenuItemClickListener false

                }

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