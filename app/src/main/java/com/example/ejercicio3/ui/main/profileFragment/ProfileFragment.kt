package com.example.ejercicio3.ui.main.profileFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ejercicio3.R
import com.example.ejercicio3.databinding.ActivityProfileBinding
import com.example.ejercicio3.data.entities.User
import com.example.ejercicio3.data.local.SharedPreferencesManager
import com.example.ejercicio3.ui.main.MainActivity
import com.example.ejercicio3.ui.start.StartActivity

class ProfileFragment : Fragment() {
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

        binding.btnClear.setOnClickListener{ goToStart() }
    }

    fun goToStart(){
        val i = Intent(activity, StartActivity::class.java)
        SharedPreferencesManager.clearData(activity!!)
        MainActivity.shopBox.clear()
        startActivity(i)
    }

    fun bindUserData(){
        val user : User = SharedPreferencesManager.getUser(activity!!)!!
        binding.tvUsername.text = "Hola, ${user.username}"
    }

    fun setAppBar(){
        val toolbar = binding.tvToolbar
        toolbar.text = this.getString(R.string.profile)
        toolbar.setTextColor(activity!!.getColorStateList(R.color.textPrimary))
    }
}