package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio3.databinding.ActivitySplashBinding
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.local.SharedPreferencesManager

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user : User? = SharedPreferencesManager.getUser(this@SplashActivity)

        if(user != null) {
            goToHome()
        }
        else {
            goToStart()
        }
    }

    private fun goToHome(){
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
    }

    private fun goToStart(){
        startActivity(Intent(this@SplashActivity, StartActivity::class.java))
    }
}