package com.leejinsil.keepthetime

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityAddStartPlaceBinding
import com.leejinsil.keepthetime.datas.BasicResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStartPlaceActivity : BaseActivity() {

    lateinit var binding : ActivityAddStartPlaceBinding

    lateinit var naverMap : NaverMap

    var marker: Marker? = null

    var mSelectedLatLng : LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_start_place)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.txtScrollHelp.setOnTouchListener { view, motionEvent ->
            binding.scrollView.requestDisallowInterceptTouchEvent(true)
            return@setOnTouchListener false
        }

        binding.btnStartPlaceSave.setOnClickListener {

            postStartPlaceToServer()

        }

    }

    override fun setValues() {

        actionBarTitle.setText(R.string.actionbar_title_add_start_place)

        getNaverMapView()

    }

    fun getNaverMapView () {

        binding.naverMapView.getMapAsync {

            naverMap = it

            naverMap.setOnMapClickListener { pointF, latLng ->

                if (marker == null) {
                    marker = Marker()
                }

                marker!!.position = latLng
                marker!!.map = naverMap

                mSelectedLatLng = latLng

            }


        }

    }

    fun postStartPlaceToServer(){

        val inputStartPlace = binding.edtStartPlace.text.toString()
        
        if (inputStartPlace.isEmpty()){
            Toast.makeText(mContext, "출발장소를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (mSelectedLatLng == null) {
            Toast.makeText(mContext, "지도를 클릭하여 출발장소를 설정해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        
        var inputChecked = binding.checkBasicStartPlace.isChecked

        binding.checkBasicStartPlace.setOnCheckedChangeListener { compoundButton, b ->
            inputChecked = b
        }
        
        apiList.postRequestAddPlace(
            inputStartPlace,
            mSelectedLatLng!!.latitude,
            mSelectedLatLng!!.longitude,
            inputChecked
        ).enqueue( object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                
                if (response.isSuccessful) {

                    Toast.makeText(mContext, "출발장소가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                    
                }
                
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                
            }
        })

    }

}