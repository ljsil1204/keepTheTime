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
import com.leejinsil.keepthetime.datas.UserData
import com.leejinsil.keepthetime.fragments.FriendRequestedFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendRequestedRecyclerAdapter(
    val mContext : Context,
    val mList : List<UserData>
) : RecyclerView.Adapter<FriendRequestedRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val txtNickname = view.findViewById<TextView>(R.id.txtNickname)
        val imgSocialLoginLogo = view.findViewById<ImageView>(R.id.imgSocialLoginLogo)
        val txtEmail = view.findViewById<TextView>(R.id.txtEmail)
        val btnAccept = view.findViewById<Button>(R.id.btnAccept)
        val btnDeny = view.findViewById<Button>(R.id.btnDeny)

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

            val ocl = View.OnClickListener {

                val tagStr = it.tag.toString()

                val retrofit = ServerAPI.getRetrofit(mContext)
                val apiList = retrofit.create(APIList::class.java)

                apiList.putRequestAcceptOrDenyFriendRequest(data.id, tagStr).enqueue(object : Callback<BasicResponse>{
                    override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                        if (response.isSuccessful) {

                            if (tagStr == "수락") {
                                Toast.makeText(mContext, "친구가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                            }

                            if (tagStr == "거절") {
                                Toast.makeText(mContext, "친구요청을 거절하였습니다.", Toast.LENGTH_SHORT).show()
                            }

                            FriendRequestedFragment.frag.getFriendRequestedFromServer()
                            
                        }

                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }

                })

            }

            btnAccept.setOnClickListener(ocl)
            btnDeny.setOnClickListener(ocl)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.friend_requested_list_item, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mList[position]
        holder.bind(data)

    }

    override fun getItemCount() = mList.size

}