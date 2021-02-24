package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio3.R
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.local.SharedPreferencesManager

class StartActivity : AppCompatActivity() {
    private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user : User? = sharedPrefManager.getUser(this@StartActivity)

        if(user != null) goToHome()
        else {
            setTheme(R.style.ThemeHome)
            setContentView(R.layout.activity_start)

            val btnLogin = findViewById<Button>(R.id.btnLogin)
            btnLogin.setOnClickListener{ goToLogin() }

            val btnSingUp = findViewById<Button>(R.id.btnSingUp)
            btnSingUp.setOnClickListener{ goToSingup() }
        }
    }

    private fun goToHome(){
        val i = Intent(this@StartActivity, HomeActivity::class.java)
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