package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.ejercicio3.R
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.local.SharedPreferencesManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : Fragment() {
    private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager

    companion object {
        fun newInstance(): ProfileActivity = ProfileActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.activity_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAppBar()
        bindUserData()

        val btnClear = activity!!.findViewById<Button>(R.id.btnClear)

        btnClear.setOnClickListener{ goToStart() }
    }

    fun goToStart(){
        val i = Intent(activity, StartActivity::class.java)
        sharedPrefManager.clearData(activity!!)
        startActivity(i)
    }

    fun bindUserData(){
        val user : User = sharedPrefManager.getUser(activity!!)!!
        val tvUsername = activity!!.findViewById<TextView>(R.id.tvUsername)
        tvUsername.text = "Hola, ${user.username}"
    }

    fun setAppBar(){
        val toolbar = activity!!.findViewById<TextView>(R.id.tvToolbar)
        toolbar.text = this.getString(R.string.profile)
        toolbar.setTextColor(activity!!.getColorStateList(R.color.textPrimary))
    }
}