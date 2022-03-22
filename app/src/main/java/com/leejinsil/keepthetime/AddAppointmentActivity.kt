package com.leejinsil.keepthetime

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.leejinsil.keepthetime.databinding.ActivityAddAppointmentBinding
import java.text.SimpleDateFormat
import java.util.*

class AddAppointmentActivity : BaseActivity() {

    lateinit var binding : ActivityAddAppointmentBinding

    val mSelectedAppointmentTime = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_appointment)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        날짜 선택 -> DatePickerDialog
        binding.btnDay.setOnClickListener {

            val dsl = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                    mSelectedAppointmentTime.set(year, month, dayOfMonth)
                    getTodayFormat()

                }
            }

            val dpd = DatePickerDialog(
                mContext,
                dsl,
                mSelectedAppointmentTime.get(Calendar.YEAR),
                mSelectedAppointmentTime.get(Calendar.MONTH),
                mSelectedAppointmentTime.get(Calendar.DAY_OF_MONTH)
            ).show()

        }

        binding.btnAppointmentSave.setOnClickListener {

            val inputTitle = binding.edtTitle.text.toString()
            val inputPlace = binding.edtPlace.text.toString()

        }

    }

    override fun setValues() {

        getTodayFormat()

    }

    fun getTodayFormat () {

//        오늘 날짜가 나오도록
        val sdf = SimpleDateFormat("yy/MM/dd (E)")
        binding.btnDay.text = sdf.format(mSelectedAppointmentTime.time)
    }

}