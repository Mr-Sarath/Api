package com.example.api.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.api.R
import com.example.api.databinding.ActivitySplashBinding
import com.example.api.utils.logThis
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private var binding: ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        handleEvents()

    }

    private fun handleEvents() {
        lifecycleScope.launch {
            delay(3000L)

            val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
            logThis("isLoggedIn----------------------------------: $isLoggedIn")

            val intent = if (isLoggedIn) {
                Intent(this@SplashActivity, MainActivity::class.java)
            } else {
                Intent(this@SplashActivity, LoginActivity::class.java)
            }

            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
    }
}