package com.example.ejercicio3.ui.start.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio3.databinding.ActivitySplashBinding
import com.example.ejercicio3.data.entities.User
import com.example.ejercicio3.data.local.SharedPreferencesManager
import com.example.ejercicio3.ui.main.MainActivity
import com.example.ejercicio3.ui.start.StartActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user : User? = SharedPreferencesManager.getUser(this@SplashActivity)

        Handler(Looper.getMainLooper()).postDelayed({
            if(user != null) {
                goToHome()
            }
            else {
                goToStart()
            }
            finish()
        },1500)
    }

    private fun goToHome(){
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
    }

    private fun goToStart(){
        startActivity(Intent(this@SplashActivity, StartActivity::class.java))
    }
}