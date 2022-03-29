package com.leejinsil.keepthetime

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.leejinsil.keepthetime.databinding.ActivityEditProfileBinding
import com.leejinsil.keepthetime.utils.URIPathHelper
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class EditProfileActivity : BaseActivity() {

    lateinit var binding : ActivityEditProfileBinding

    val REQ_CODE_GALLERY = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnImageUpload.setOnClickListener {
            getProfileImage()
        }

    }

    override fun setValues() {



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

//                val file = File(URIPathHelper().getPath(mContext, selectedImageUri))
//
//                val fileReqBody = RequestBody.create(MediaType.get("image/*"), file)
//
//                val mutiPartBody = MultipartBody.Part.createFormData("profile_image", "myProfile.jpg", fileReqBody)

            }

        }

    }

}