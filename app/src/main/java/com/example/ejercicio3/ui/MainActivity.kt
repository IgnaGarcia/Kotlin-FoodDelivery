package com.example.ejercicio3.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ejercicio3.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navBottom = findViewById<BottomNavigationView>(R.id.navBottom)
        navBottom.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.icHome -> {
                    val fragment = HomeActivity.newInstance()
                    openFragment(fragment)
                    true
                }
                R.id.icProfile -> {
                    val fragment = ProfileActivity.newInstance()
                    openFragment(fragment)
                    true
                }
                else -> false
            }
        }

        navBottom.selectedItemId = R.id.icHome
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}