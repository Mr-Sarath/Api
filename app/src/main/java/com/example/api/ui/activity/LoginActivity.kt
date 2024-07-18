package com.example.api.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.api.R
import com.example.api.databinding.ActivityLoginBinding
import com.example.api.utils.shortToast

class LoginActivity : AppCompatActivity() {
    private var binding: ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        handleEvents()

    }

    private fun handleEvents() {
        binding?.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                if (validation(email, password)) {
                    if (email == "sarath.k@meridian.net.in" && password == "12345678") {
                        shortToast("Login successful")

                        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putBoolean("isLoggedIn", true)
                            putString("userEmail", email)
                            putString("userPassword", password)
                            apply()
                        }

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
                    } else {
                        shortToast("Invalid email or password")
                    }
                } else {
                    shortToast("Enter valid email and password")
                }
            }
        }
    }

    private fun validation(email: String, password: String): Boolean {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            shortToast("Enter valid email")
            return false
        } else if (password.isEmpty()) {
            shortToast("Enter valid password")
            return false
        }
        return true
    }
}