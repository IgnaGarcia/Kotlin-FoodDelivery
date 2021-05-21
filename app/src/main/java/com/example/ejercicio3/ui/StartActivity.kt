package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio3.R
import com.example.ejercicio3.databinding.ActivityStartBinding
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.local.SharedPreferencesManager

class StartActivity : AppCompatActivity() {
    private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager
    private lateinit var binding : ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        val user : User? = sharedPrefManager.getUser(this@StartActivity)

        if(user != null) goToHome()
        else {
            setTheme(R.style.ThemeHome)
            setContentView(binding.root)

            val btnLogin = binding.btnLogin
            btnLogin.setOnClickListener{ goToLogin() }

            val btnSingUp = binding.btnSingUp
            btnSingUp.setOnClickListener{ goToSingup() }
        }
    }

    private fun goToHome(){
        val i = Intent(this@StartActivity, MainActivity::class.java)
        startActivity(i)
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