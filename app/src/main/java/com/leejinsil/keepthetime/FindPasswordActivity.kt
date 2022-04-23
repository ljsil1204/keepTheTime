package com.leejinsil.keepthetime

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityFindPasswordBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindPasswordActivity : BaseActivity() {

    lateinit var binding: ActivityFindPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_password)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnFindPw.setOnClickListener {
            postFindPwToSever()
        }

    }

    override fun setValues() {

        actionBarTitle.setText(R.string.actionbar_title_find_password)

    }

    fun postFindPwToSever() {

        val inputId = binding.edtId.text.toString()
        val inputNickname = binding.edtNickname.text.toString()

        apiList.postRequestFindPassword(inputId, inputNickname).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful) {

                    val br = response.body()!!

                    Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()
                    finish()

                }
                else {

                    val br = response.errorBody()!!.string()
                    val jsonObj = JSONObject(br)
                    val message = jsonObj.getString("message")

                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()

                }


            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })

    }

}