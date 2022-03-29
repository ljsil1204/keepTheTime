package com.leejinsil.keepthetime

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.leejinsil.keepthetime.databinding.ActivityEditProfileBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import com.leejinsil.keepthetime.datas.UserData
import com.leejinsil.keepthetime.utils.URIPathHelper
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditProfileActivity : BaseActivity() {

    lateinit var binding : ActivityEditProfileBinding

    lateinit var mUserData: UserData

    val REQ_CODE_GALLERY = 1000

    lateinit var mSelectedImageUri : Uri

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

        binding.btnProfileSave.setOnClickListener {

            putProfileImageToServer()

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

        val pl = object : PermissionListener {
            override fun onPermissionGranted() {

                val myIntent = Intent()
                myIntent.action = Intent.ACTION_PICK
                myIntent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
                startActivityForResult( myIntent, REQ_CODE_GALLERY )

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(mContext, "갤러리 조회 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }

        }

        TedPermission.create()
            .setPermissionListener( pl )
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()

    }

    fun putProfileImageToServer(){

        val file = File(URIPathHelper().getPath(mContext, mSelectedImageUri))
        val fileReqBody = RequestBody.create(MediaType.get("image/*"), file)
        val mutiPartBody = MultipartBody.Part.createFormData("profile_image", "myProfile.jpg", fileReqBody)

        apiList.putRequestProfileImg(mutiPartBody).enqueue( object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if(response.isSuccessful){

                    Toast.makeText(mContext, "프로필 사진이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    
                }

                Log.d("서버 응답", response.toString())

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_GALLERY) {

            if (resultCode == Activity.RESULT_OK) {

                mSelectedImageUri = data?.data!!

                Glide.with(mContext).load(mSelectedImageUri).into(binding.imgProfile)

                binding.btnImageDelete.visibility = View.VISIBLE


            }

        }

    }

}