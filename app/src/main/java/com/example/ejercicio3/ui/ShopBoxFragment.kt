package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejercicio3.R
import com.example.ejercicio3.adapters.PlatesCardAdapter
import com.example.ejercicio3.adapters.ShopBoxAdapter
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.local.SharedPreferencesManager

class ShopBoxFragment : Fragment(), ShopBoxAdapter.OnClickPlate {
    var shopBoxAdapter : ShopBoxAdapter? = null

    companion object {
        fun newInstance(): ShopBoxFragment = ShopBoxFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.activity_shopbox, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAppBar()
        setBottomText()
        chargePlates()
    }

    //Intent a partir del click en una tarjeta del RecyclerView
    override fun onClickPlate(plate : Int){
        val i = Intent(activity, PlateActivity::class.java)
        i.putExtra(PlateActivity.PLATE_KEY, plate)
        startActivity(i)
    }

    fun setPlatesAdapter(rvPlatesCards : RecyclerView){
        shopBoxAdapter = ShopBoxAdapter(MainActivity.shopBox.plateList, this)

        rvPlatesCards.adapter = shopBoxAdapter
        rvPlatesCards.layoutManager = LinearLayoutManager(
                activity, LinearLayoutManager.VERTICAL, false)

    }

    fun chargePlates(){
        val rvPlatesCards = activity!!.findViewById<RecyclerView>(R.id.rvPlatesCards)
        setPlatesAdapter(rvPlatesCards)
    }

    fun setAppBar(){
        val toolbar = activity!!.findViewById<TextView>(R.id.tvToolbar)
        toolbar.text = this.getString(R.string.shopBag)
        toolbar.setTextColor(activity!!.getColorStateList(R.color.textPrimary))
    }

    fun setBottomText(){
        val btnFinished = activity!!.findViewById<TextView>(R.id.btnFinish)
        btnFinished.text = this.getString(R.string.buyFinished) + MainActivity.shopBox.calcTotal()
    }
}