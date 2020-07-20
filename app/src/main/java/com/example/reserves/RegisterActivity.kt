package com.example.reserves

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_user_registration.*


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)

        val name = findViewById<EditText>(R.id.nameEditText)
        val surname = findViewById<EditText>(R.id.surnameEditText)
        val email = findViewById<EditText>(R.id.emailEditText)
        val document = findViewById<EditText>(R.id.documentEditText)
        val phone = findViewById<EditText>(R.id.phoneEditText)
        val password = findViewById<EditText>(R.id.passwordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            progress_bar.visibility = View.VISIBLE

            if (isValidData(
                name.text.toString().trim(),
                surname.text.toString().trim(),
                email.text.toString().trim(),
                document.text.toString().trim(),
                phone.text.toString().trim(),
                password.text.toString().trim()
            )) {
                // Valid values, call to service
                createUser(
                    name = name.text.toString().trim(),
                    surname = surname.text.toString().trim(),
                    email = email.text.toString().trim(),
                    document = document.text.toString().trim().toInt(),
                    phone = phone.text.toString().trim(),
                    password = password.text.toString().trim()
                )
            }
            progress_bar.visibility = View.GONE
        }
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target)
            .matches()
    }

    private fun isANumber(value: String): Boolean {
        return value?.toIntOrNull()?.let { true } == true
    }

    private fun isValidData(
        name: String,
        surname: String,
        email: String,
        document: String,
        phone: String,
        password: String
    ): Boolean {
        if (name.isEmpty() || isANumber(name)) {
            Toast.makeText(
                applicationContext,
                getString(R.string.invalidName),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (surname.isEmpty() || isANumber(
                surname
            )
        ) {
            Toast.makeText(
                applicationContext,
                getString(R.string.invalidSurname),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (email.isEmpty() || !isValidEmail(
                email
            )
        ) {
            Toast.makeText(
                applicationContext,
                getString(R.string.invalidEmail),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (document.isEmpty() || !isANumber(
                document
            )
        ) {
            Toast.makeText(
                applicationContext,
                getString(R.string.invalidDocument),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (phone.isEmpty() || !isANumber(phone)
        ) {
            Toast.makeText(
                applicationContext,
                getString(R.string.invalidPhone),
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(
                applicationContext,
                getString(R.string.invalidPassword),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    private fun createUser(
        name: String,
        surname: String,
        email: String,
        phone: String,
        document: Int,
        password: String
    ) {
        val apiService = RestApiService()
        val user = UserData(
            _id = null,
            nombre = name,
            apellido = surname,
            email = email,
            estado = "ACTIVO",
            telefono = phone,
            documento = document,
            contraseña = password
        )

        progress_bar.visibility = View.VISIBLE
        apiService.createUser(user) {
            if (it != null) {
                // Save in preferences the user
                val sharedPreferences = getSharedPreferences("User_Info", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("userId", it._id)
                editor.apply()

                // Push to home activity
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.errorRegisteringUser),
                    Toast.LENGTH_LONG
                ).show()
            }
            progress_bar.visibility = View.GONE
        }
    }
}