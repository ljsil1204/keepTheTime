package com.leejinsil.keepthetime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityEditPasswordBinding

class EditPasswordActivity : BaseActivity() {
    lateinit var binding : ActivityEditPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_password)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}