package com.leejinsil.keepthetime

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.leejinsil.keepthetime.adapters.SearchedUserRecyclerAdapter
import com.leejinsil.keepthetime.databinding.ActivitySearchUserBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserActivity : BaseActivity() {

    lateinit var binding : ActivitySearchUserBinding

    lateinit var mSearchUserAdapter : SearchedUserRecyclerAdapter

    var mSearchUserList = ArrayList<UserData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_user)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnSearch.setOnClickListener {
            getSearchUserFormServer()
        }

    }

    override fun setValues() {

        actionBarTitle.setText(R.string.actionbar_title_add_friend)

        mSearchUserAdapter = SearchedUserRecyclerAdapter(mContext, mSearchUserList)
        binding.userListRecyclerView.adapter = mSearchUserAdapter
        binding.userListRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }

    fun getSearchUserFormServer() {

        val inputNickname = binding.edtNickname.text.toString()

        apiList.getRequestSearchUser(inputNickname).enqueue( object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful) {

                    val br = response.body()!!

                    mSearchUserList.clear()
                    mSearchUserList.addAll(br.data.users)

                    mSearchUserAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })

    }

}