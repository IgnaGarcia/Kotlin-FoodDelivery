package com.example.ejercicio3.ui.start

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio3.databinding.ActivityStartBinding
import com.example.ejercicio3.ui.start.signup.SignupActivity
import com.example.ejercicio3.ui.start.login.LoginActivity

class StartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{ goToLogin() }
        binding.btnSingUp.setOnClickListener{ goToSingup() }
    }

    private fun goToLogin(){
        startActivity(Intent(this@StartActivity, LoginActivity::class.java))
    }

    private fun goToSingup(){
        startActivity(Intent(this@StartActivity, SignupActivity::class.java))
    }
}