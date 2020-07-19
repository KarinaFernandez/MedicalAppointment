package com.example.reserves

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_user_registration.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email = findViewById<EditText>(R.id.emailEditText)
        val password = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        val createAccountButton = findViewById<Button>(R.id.createAccountButton)
        createAccountButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            if (validaData(email.text.toString().trim(), password.text.toString().trim())) {
                loginUser(
                    email = email.text.toString().trim(),
                    password = password.text.toString().trim()
                )
            }
        }
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target)
            .matches()
    }

    private fun validaData(email: String, password: String): Boolean {
        if (email.isEmpty() || !isValidEmail(email)
        ) {
            Toast.makeText(
                applicationContext,
                getString(R.string.invalidEmail),
                Toast.LENGTH_LONG
            ).show()
            progress_bar.visibility = View.GONE
            return false
            // return@OnClickListener
        }

        if (password.length < 6) {
            Toast.makeText(
                applicationContext,
                getString(R.string.invalidPassword),
                Toast.LENGTH_LONG
            ).show()
            progress_bar.visibility = View.GONE
            return false
            //return@OnClickListener
        }
        return true
    }

    private fun loginUser(
        email: String,
        password: String
    ) {
        val apiService = RestApiService()
        val user = UserData(nombre = null,
            apellido = null,
            email = email,
            estado = "ACTIVO",
            telefono = null,
            documento = null,
            contraseña = password
        )

        apiService.loginUser(user) {
            if (it != null) {
                // Push to home activity
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.errorLoginUser),
                    Toast.LENGTH_LONG
                ).show()
            }
            progress_bar.visibility = View.GONE
        }
    }
}