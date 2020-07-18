package com.example.reserves

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createAccountButton = findViewById<Button>(R.id.button_create_account)
        createAccountButton.setOnClickListener {
            val intent = Intent(this, UserRegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}