package com.example.ejercicio3.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ejercicio3.R
import com.example.ejercicio3.data.entities.ShopBox
import com.example.ejercicio3.databinding.ActivityMainBinding
import com.example.ejercicio3.ui.main.homeFragment.HomeFragment
import com.example.ejercicio3.ui.main.profileFragment.ProfileFragment
import com.example.ejercicio3.ui.main.searchFragment.SearchFragment
import com.example.ejercicio3.ui.main.shopBoxFragment.ShopBoxFragment

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
                    openFragment(HomeFragment.newInstance())
                    showBottomNavView()
                    true
                }
                R.id.icProfile -> {
                    openFragment(ProfileFragment.newInstance())
                    true
                }
                R.id.icBox -> {
                    openFragment(ShopBoxFragment.newInstance())
                    true
                }
                R.id.icSearch -> {
                    openFragment(SearchFragment.newInstance())
                    true
                }
                else -> {
                    openFragment(HomeFragment.newInstance())
                    showBottomNavView()
                    true
                }
            }
        }

        navBottom.selectedItemId = intent.extras?.getInt(SHOP_KEY) ?: R.id.icHome
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_container, fragment)
            if (fragment is ShopBoxFragment) {
                hideBottomNavView()
            } else if(!fragment.isAdded){
                showBottomNavView()
            }
            commit()
        }
    }

    private fun hideBottomNavView() {
        binding.navBottom.visibility = View.GONE
    }

    fun showBottomNavView() {
        binding.navBottom.visibility = View.VISIBLE
    }
}