package com.example.reserves

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        val doctorName=intent.getStringExtra("doctorName")
        val doctorSurname=intent.getStringExtra("doctorSurname")

        val scheduleDoctor = findViewById<TextView>(R.id.scheduleDoctor)
        val doctorInfo = findViewById<TextView>(R.id.doctorInfo)
        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val date = findViewById<TextView>(R.id.date)

        scheduleDoctor.text = "Reserva para el doctor: "
        doctorInfo.text = StringBuilder().append(doctorName).append(" ").append(doctorSurname).toString()

        val today = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        date.text = currentDate

        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            val month = month + 1
            val selectedDay = "$day/$month/$year"
            date.text = selectedDay
        }
    }
}