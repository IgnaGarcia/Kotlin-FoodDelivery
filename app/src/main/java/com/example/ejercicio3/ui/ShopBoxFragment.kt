package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ejercicio3.R
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.local.SharedPreferencesManager

class ShopBoxFragment : Fragment() {

    companion object {
        fun newInstance(): ShopBoxFragment = ShopBoxFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.activity_shopbox, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAppBar()

        val btnClear = activity!!.findViewById<Button>(R.id.btnClear)

    }

    fun setAppBar(){
        val toolbar = activity!!.findViewById<TextView>(R.id.tvToolbar)
        toolbar.text = this.getString(R.string.shopBag)
        toolbar.setTextColor(activity!!.getColorStateList(R.color.textPrimary))
    }
}