package com.example.ejercicio3.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ejercicio3.R
import com.example.ejercicio3.entities.ShopBox
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    companion object{
        var shopBox = ShopBox()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navBottom = findViewById<BottomNavigationView>(R.id.navBottom)
        navBottom.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.icHome -> {
                    val fragment = HomeFragment.newInstance()
                    openFragment(fragment)
                    true
                }
                R.id.icProfile -> {
                    val fragment = ProfileFragment.newInstance()
                    openFragment(fragment)
                    true
                }
                R.id.icBox -> {
                    val fragment = ShopBoxFragment.newInstance()
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