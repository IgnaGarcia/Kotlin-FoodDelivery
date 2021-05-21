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
import com.example.ejercicio3.databinding.ActivityShopboxBinding
import com.example.ejercicio3.entities.Plate
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.local.SharedPreferencesManager
import com.example.ejercicio3.network.responses.PlateResponse

class ShopBoxFragment : Fragment(), ShopBoxAdapter.OnClickPlate {
    private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager
    var shopBoxAdapter : ShopBoxAdapter? = null
    private var _binding : ActivityShopboxBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): ShopBoxFragment = ShopBoxFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?)
            : View? {
        _binding = ActivityShopboxBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

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
        val rvPlatesCards = binding.llPlateList.rvPlatesCards
        setPlatesAdapter(rvPlatesCards)
    }

    fun setAppBar(){
        val toolbar = binding.tvToolbar
        toolbar.text = this.getString(R.string.shopBag)
        toolbar.setTextColor(activity!!.getColorStateList(R.color.textPrimary))
    }

    fun setBottomText(){
        val btnFinished = binding.btnFinish
        btnFinished.text =
                this.getString(R.string.buyFinished) + " $" + calcTotal(MainActivity.shopBox.plateList)
    }

    fun calcTotal(plateList : MutableList<Plate>) : Double{
        var acc = 0.0
        for(plate in plateList){
            acc += plate.pricePerServing
            println("precio total: $acc")
        }
        return acc
    }
}