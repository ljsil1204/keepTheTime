package com.leejinsil.keepthetime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityEditPasswordBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPasswordActivity : BaseActivity() {
    lateinit var binding : ActivityEditPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_password)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnEditSave.setOnClickListener {

            patchRequestEditPasswordToServer()

        }

    }

    override fun setValues() {

    }

    fun patchRequestEditPasswordToServer () {

        val inputCurrentPw = binding.currentPw.text.toString()
        val inputnewPw = binding.newPw.text.toString()

        apiList.patchRequestEditPassword(inputCurrentPw, inputnewPw).enqueue( object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful) {
                    Toast.makeText(mContext, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })

    }

}