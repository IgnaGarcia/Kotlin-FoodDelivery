package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio3.R
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.local.SharedPreferencesManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setAppBar()
        val user : User = sharedPrefManager.getUser(this@ProfileActivity)!!

        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        tvUsername.text = "Hola, ${user.username}"
        val btnClear = findViewById<Button>(R.id.btnClear)

        btnClear.setOnClickListener{
            val i = Intent(this@ProfileActivity, StartActivity::class.java)
            sharedPrefManager.clearData(this@ProfileActivity)
            startActivity(i)
        }

        val bottomAppBar = findViewById<BottomNavigationView>(R.id.navBottom)
        bottomAppBar.selectedItemId = R.id.icProfile
        bottomAppBar.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.icProfile -> true
                R.id.icHome ->  {
                    goToHome()
                    true
                }
                else -> false
            }
        }
    }

    fun goToHome(){
        val i = Intent(this@ProfileActivity, HomeActivity::class.java)
        startActivity(i)
    }

    fun setAppBar(){
        val toolbar = findViewById<TextView>(R.id.tvToolbar)
        toolbar.text = this.getString(R.string.profile)
        toolbar.setTextColor(this.getColorStateList(R.color.textPrimary))

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

}