package com.example.ejercicio3.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ejercicio3.R
import com.example.ejercicio3.databinding.ActivityMainBinding
import com.example.ejercicio3.entities.ShopBox

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    companion object{
        var shopBox = ShopBox()
        var SHOP_KEY = "shop"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navBottom = binding.navBottom
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
                R.id.icSearch -> {
                    val fragment = SearchFragment.newInstance()
                    openFragment(fragment)
                    true
                }
                else -> {
                    val fragment = HomeFragment.newInstance()
                    openFragment(fragment)
                    true
                }
            }
        }

        navBottom.selectedItemId = intent.extras?.getInt(SHOP_KEY) ?: R.id.icHome
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}