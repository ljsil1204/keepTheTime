package com.leejinsil.keepthetime.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.datas.UserData

class InviteSelectedFriendRecyclerAdapter(
    val mContext : Context,
    val mList : List<UserData>
) : RecyclerView.Adapter<InviteSelectedFriendRecyclerAdapter.MyViewHolder>() {

    private var listUserData : MutableList<UserData> = mList as MutableList<UserData>

    private lateinit var itemClickListener : ItemClickListener

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val txtNickname = view.findViewById<TextView>(R.id.txtNickname)
        val imgRemove = view.findViewById<ImageView>(R.id.imgRemove)

        fun bind(data: UserData) {

            Glide.with(mContext).load(data.profile_img).into(imgProfile)
            txtNickname.text = data.nick_name

            imgRemove.setOnClickListener {
                removeItem(position)
            }

            itemView.setOnClickListener {
                itemClickListener.onClick(it, position)

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.invite_selected_friend_list_item, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mList[position]
        holder.bind(data)

    }

    override fun getItemCount() = mList.size

    fun removeItem(position : Int) {
        listUserData.removeAt(position)
        notifyDataSetChanged()
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int)
    }

    fun setItemClickListener (itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}