package com.example.reserves

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_user_registration.*
import java.text.SimpleDateFormat
import java.util.*

class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        // Get values from Home Activity
        val doctorId=intent.getStringExtra("doctorId")
        val doctorName=intent.getStringExtra("doctorName")
        val doctorSurname=intent.getStringExtra("doctorSurname")

        val scheduleDoctor = findViewById<TextView>(R.id.scheduleDoctor)
        val doctorInfo = findViewById<TextView>(R.id.doctorInfo)
        val date = findViewById<TextView>(R.id.date)
        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val scheduleButton = findViewById<Button>(R.id.appointmentButton)

        // Load schedule info
        scheduleDoctor.text = getString(R.string.scheduleInfo)
        doctorInfo.text = StringBuilder().append(doctorName).append(" ").append(doctorSurname).toString()

        val today = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm")
        val currentDate = dateFormat.format(Date())
        date.text = currentDate

        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            val month = month + 1
            val selectedDay = "$month-$day-$year 07:00"
            date.text = selectedDay
        }

        scheduleButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("User_Info", Context.MODE_PRIVATE)
            val userId =  sharedPreferences.getString("userId", "")

            val apiService = RestApiService()
            val schedule = ScheduleData(_id = null,
            medico = doctorId,
            usuario = userId,
            fecha = date.text.toString())

            apiService.createSchedule(schedule) {
                if (it?._id != null) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.scheduleSuccessMessage),
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.scheduleFailedMessage),
                        Toast.LENGTH_LONG
                    ).show()
                }
               // progress_bar.visibility = View.GONE
            }
        }
    }
}