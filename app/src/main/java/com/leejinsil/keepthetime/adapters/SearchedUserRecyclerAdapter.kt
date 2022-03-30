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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchedUserRecyclerAdapter(
    val mContext: Context,
    val mList : List<UserData>
) : RecyclerView.Adapter<SearchedUserRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val txtNickname = view.findViewById<TextView>(R.id.txtNickname)
        val imgSocialLoginLogo = view.findViewById<ImageView>(R.id.imgSocialLoginLogo)
        val txtEmail = view.findViewById<TextView>(R.id.txtEmail)
        val btnAddFriend = view.findViewById<Button>(R.id.btnAddFriend)

        fun bind( data:UserData ){
            txtNickname.text = data.nick_name
            Glide.with(mContext).load( data.profile_img ).into(imgProfile)

            when(data.provider) {
                "default" -> {
                    imgSocialLoginLogo.visibility = View.GONE
                    txtEmail.text = data.email
                }
                "kakao" -> {
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    imgSocialLoginLogo.setImageResource(R.drawable.sns_kakao)
                    txtEmail.text = "카카오 로그인"
                }
                "facebook" ->{
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    imgSocialLoginLogo.setImageResource(R.drawable.sns_facebook)
                    txtEmail.text = "페이스북 로그인"
                }
                "naver" ->{
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    imgSocialLoginLogo.setImageResource(R.drawable.sns_naver)
                    txtEmail.text = "네이버 로그인"
                }
            }

            btnAddFriend.setOnClickListener {

                val retrofit = ServerAPI.getRetrofit(mContext)
                val apiList = retrofit.create(APIList::class.java)

                apiList.postRequestAddFriend(data.id).enqueue( object : Callback<BasicResponse>{
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {

                        if (response.isSuccessful){
                            Toast.makeText(mContext, "친구요청을 보냈습니다.", Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }

                } )
            }



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.searched_user_list_item, parent, false)
        return MyViewHolder(row)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mList[position]
        holder.bind(data)


    }

    override fun getItemCount() = mList.size

}