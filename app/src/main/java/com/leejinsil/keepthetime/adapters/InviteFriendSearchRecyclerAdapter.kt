package com.leejinsil.keepthetime.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.leejinsil.keepthetime.R
import com.leejinsil.keepthetime.api.APIList
import com.leejinsil.keepthetime.api.ServerAPI
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.UserData
import com.leejinsil.keepthetime.fragments.FriendListFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InviteFriendSearchRecyclerAdapter(
    val mContext : Context,
    val mList : List<UserData>
) : RecyclerView.Adapter<InviteFriendSearchRecyclerAdapter.MyViewHolder>(), Filterable {

    private var searchList : List<UserData>? = mList

    private lateinit var itemClickListener : ItemClickListener

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val txtNickname = view.findViewById<TextView>(R.id.txtNickname)

        fun bind(data: UserData) {

            Glide.with(mContext).load(data.profile_img).into(imgProfile)
            txtNickname.text = data.nick_name

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.invite_friend_search_list_item, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = searchList?.get(position)
        holder.bind(data!!)

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

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

}