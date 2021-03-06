package com.leejinsil.keepthetime.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.datas.UserData

class InviteSearchFriendRecyclerAdapter(
    val mContext : Context,
    val mList : List<UserData>
) : RecyclerView.Adapter<InviteSearchFriendRecyclerAdapter.MyViewHolder>(), Filterable {

    private var searchList : List<UserData>? = mList

    private lateinit var itemClickListener : ItemClickListener

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val txtNickname = view.findViewById<TextView>(R.id.txtNickname)
//        val checkBoxFriend = view.findViewById<CheckBox>(R.id.checkBoxFriend)


        fun bind(data: UserData) {

            Glide.with(mContext).load(data.profile_img).into(imgProfile)
            txtNickname.text = data.nick_name

            itemView.setOnClickListener {
                itemClickListener.onClick(it, position)

            }

//            checkBoxFriend.tag = data.id

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.invite_search_friend_list_item, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = searchList?.get(position)
        holder.bind(data!!)

    }

    override fun getItemCount() = searchList?.size!!

    override fun getFilter(): Filter {

        return object : Filter(){

            override fun performFiltering(charSequence: CharSequence?): FilterResults {

                val charString = charSequence.toString()

                searchList = if (charString.isEmpty()) {
                    mList
                } else {

                    val filterList = ArrayList<UserData>()

                    if (mList != null) {
                        for (listRow in mList) {
                            if (listRow.nick_name.toLowerCase().contains(charString.toLowerCase())) {
                                filterList.add(listRow)
                            }
                        }
                    }
                    filterList
                }

                val filterResults = FilterResults()
                filterResults.values = searchList
                return filterResults
            }
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {

                searchList = filterResults?.values as ArrayList<UserData>
                notifyDataSetChanged()

            }

        }

    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int)
    }

    fun setItemClickListener (itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun getFriend(position: Int) : UserData {
        return searchList!![position]
    }

}