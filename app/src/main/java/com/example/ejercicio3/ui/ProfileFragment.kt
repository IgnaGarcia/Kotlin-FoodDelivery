package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ejercicio3.R
import com.example.ejercicio3.databinding.ActivityProfileBinding
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.local.SharedPreferencesManager

class ProfileFragment : Fragment() {
    private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager
    private var _binding : ActivityProfileBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? {
        _binding = ActivityProfileBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAppBar()
        bindUserData()

        val btnClear = binding.btnClear
        btnClear.setOnClickListener{ goToStart() }
    }

    fun goToStart(){
        val i = Intent(activity, StartActivity::class.java)
        sharedPrefManager.clearData(activity!!)
        startActivity(i)
    }

    fun bindUserData(){
        val user : User = sharedPrefManager.getUser(activity!!)!!
        val tvUsername = binding.tvUsername
        tvUsername.text = "Hola, ${user.username}"
    }

    fun setAppBar(){
        val toolbar = binding.tvToolbar
        toolbar.text = this.getString(R.string.profile)
        toolbar.setTextColor(activity!!.getColorStateList(R.color.textPrimary))
    }
}