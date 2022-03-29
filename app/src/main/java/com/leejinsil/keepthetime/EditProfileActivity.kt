package com.leejinsil.keepthetime

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.leejinsil.keepthetime.databinding.ActivityEditProfileBinding
import com.leejinsil.keepthetime.datas.UserData
import com.leejinsil.keepthetime.utils.URIPathHelper
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class EditProfileActivity : BaseActivity() {

    lateinit var binding : ActivityEditProfileBinding

    lateinit var mUserData: UserData

    val REQ_CODE_GALLERY = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        mUserData = intent.getSerializableExtra("profile") as UserData
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnImageUpload.setOnClickListener {
            getProfileImage()
        }

        binding.btnImageDelete.setOnClickListener {

            val defaultImageUri = "https://s3.ap-northeast-2.amazonaws.com/neppplus.finalproject.202109/profile_imgs/default_profile_icon.jpg"
            Glide.with(mContext).load(defaultImageUri).into(binding.imgProfile)

            binding.btnImageDelete.visibility = View.GONE

        }

    }

    override fun setValues() {

        setUi()

    }

    fun setUi () {

        Glide.with(mContext).load(mUserData.profile_img).into(binding.imgProfile)
        binding.edtNickname.setText(mUserData.nick_name)

    }

    fun getProfileImage() {

        val myIntent = Intent()
        myIntent.action = Intent.ACTION_PICK
        myIntent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult( myIntent, REQ_CODE_GALLERY )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_GALLERY) {

            if (resultCode == Activity.RESULT_OK) {

                val selectedImageUri = data?.data!!

                Glide.with(mContext).load(selectedImageUri).into(binding.imgProfile)

                binding.btnImageDelete.visibility = View.VISIBLE

//                val file = File(URIPathHelper().getPath(mContext, selectedImageUri))
//
//                val fileReqBody = RequestBody.create(MediaType.get("image/*"), file)
//
//                val mutiPartBody = MultipartBody.Part.createFormData("profile_image", "myProfile.jpg", fileReqBody)

            }

        }

    }

}