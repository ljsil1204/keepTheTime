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

class FriendListRecyclerAdapter(
    val mContext : Context,
    val mList : List<UserData>
) : RecyclerView.Adapter<FriendListRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val txtNickname = view.findViewById<TextView>(R.id.txtNickname)
        val imgSocialLoginLogo = view.findViewById<ImageView>(R.id.imgSocialLoginLogo)
        val txtEmail = view.findViewById<TextView>(R.id.txtEmail)

        fun bind(data: UserData) {

            Glide.with(mContext).load(data.profile_img).into(imgProfile)
            txtNickname.text = data.nick_name

            when(data.provider){
                "default" -> {
                    txtEmail.text = data.email
                    imgSocialLoginLogo.visibility = View.GONE
                }
                "kakao" -> {
                    txtEmail.text = "카카오로그인"
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    Glide.with(mContext).load(R.drawable.sns_kakao).into(imgSocialLoginLogo)
                }
                "facebook" ->{
                    txtEmail.text = "페북 로그인"
                    Glide.with(mContext).load(R.drawable.sns_facebook).into(imgSocialLoginLogo)
                }
                "naver" -> {
                    txtEmail.text = "네이버 로그인"
                    Glide.with(mContext).load(R.drawable.sns_naver).into(imgSocialLoginLogo)
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.friend_list_item, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mList[position]
        holder.bind(data)

    }

    override fun getItemCount() = mList.size

}