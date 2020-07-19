package com.example.reserves

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_user_registration.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val apiService = RestApiService()

        apiService.getDoctors() {
            if (it != null) {
                print(it)
            } else {
                Toast.makeText(
                    applicationContext,
                   "No se pudo obtener la lista de doctores",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}