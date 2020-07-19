package com.example.reserves

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
            if (name.text.toString().trim().isEmpty() || isANumber(name.text.toString().trim())) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.invalidName),
                    Toast.LENGTH_LONG
                ).show()
                // return@OnClickListener
            }

            if (surname.text.toString().trim().isEmpty() || isANumber(
                    surname.text.toString().trim()
                )
            ) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.invalidSurname),
                    Toast.LENGTH_LONG
                ).show()
                // return@OnClickListener
            }

            if (email.text.toString().trim().isEmpty() || !isValidEmail(
                    email.text.toString().trim()
                )
            ) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.invalidEmail),
                    Toast.LENGTH_LONG
                ).show()
                // return@OnClickListener
            }

            if (document.text.toString().trim().isEmpty() || !isANumber(
                    document.text.toString().trim()
                )
            ) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.invalidDocument),
                    Toast.LENGTH_LONG
                ).show()
                //  return@OnClickListener
            }

            if (phone.text.toString().trim().isEmpty() || !isANumber(
                    phone.text.toString().trim()
                )
            ) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.invalidPhone),
                    Toast.LENGTH_LONG
                ).show()
                // return@OnClickListener
            }

            if (password.text.toString().trim().length < 6) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.invalidPassword),
                    Toast.LENGTH_LONG
                ).show()
                //return@OnClickListener
            }

            progress_bar.visibility = View.VISIBLE

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
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target)
            .matches()
    }

    private fun isANumber(value: String): Boolean {
        return value?.toIntOrNull()?.let { true } == true
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
            nombre = name,
            apellido = surname,
            email = email,
            estado = "ACTIVO",
            telefono = phone,
            documento = document,
            contraseña = password
        )

        apiService.createUser(user) {
            if (it != null) {
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