package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio3.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val btnLogin = binding.btnLogin
        btnLogin.setOnClickListener{ goToLogin() }

        val btnSingUp = binding.btnSingUp
        btnSingUp.setOnClickListener{ goToSingup() }

    }

    private fun goToLogin(){
        val i = Intent(this@StartActivity, LoginActivity::class.java)
        startActivity(i)
    }

    private fun goToSingup(){
        val i = Intent(this@StartActivity, SingupActivity::class.java)
        startActivity(i)
    }
}