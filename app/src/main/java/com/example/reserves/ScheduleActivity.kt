package com.example.reserves

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import java.util.*

class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val date = findViewById<TextView>(R.id.date)

        val today = Calendar.getInstance()
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            val month = month + 1
            val msg = "$day/$month/$year"
            date.text = msg
        }
    }
}