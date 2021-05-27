package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejercicio3.R
import com.example.ejercicio3.adapters.PlatesShopCardAdapter
import com.example.ejercicio3.databinding.ActivityCheckoutBinding
import com.example.ejercicio3.entities.Plate
import com.example.ejercicio3.entities.ShopBox

class CheckoutActivity : AppCompatActivity(), PlatesShopCardAdapter.OnClickPlate,
        ShopBox.OnCountChange {
    private var platesShopCardAdapter : PlatesShopCardAdapter? = null
    private lateinit var binding : ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MainActivity.shopBox.onCountChange = this

        binding.btnAddPlates.setOnClickListener{ goToPlateList() }
        setAppBar()
        setPlatesAdapter(binding.llPlateList.rvPlatesShopCards, MainActivity.shopBox.plateList)
        setBottomText()
    }

    fun goToPlateList(){
        val i = Intent(this, PlatesListActivity::class.java)
        i.putExtra(PlatesListActivity.QUERY_ID, 4)
        startActivity(i)
    }

    fun setPlatesAdapter(rvPlatesCards : RecyclerView, plates : List<Plate>){
        platesShopCardAdapter = PlatesShopCardAdapter(plates, this, 2)

        rvPlatesCards.adapter = platesShopCardAdapter
        rvPlatesCards.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onClickPlate(plate: Int) {
        val i = Intent(this, PlateActivity::class.java)
        i.putExtra(PlateActivity.PLATE_KEY, plate)
        startActivity(i)
    }

    override fun onCountChange(){
        val tvItemTotalPrice = binding.tvItemTotalPrice
        val tvTotalPrice = binding.tvTotalPrice
        val tvFinalPrice = binding.tvFinalPrice

        val total = MainActivity.shopBox.calcTotal()
        tvItemTotalPrice.text = String.format("$%.2f", total)
        tvTotalPrice.text = String.format("$%.2f", total)
        tvFinalPrice.text = String.format("$%.2f", total)
    }

    fun setAppBar(){
        val toolbar = binding.appBar.tvToolbar
        toolbar.text = this.getString(R.string.checkout)

        toolbar.setTextColor(this.getColorStateList(R.color.textPrimary))

        setSupportActionBar(binding.appBar.toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun setBottomText(){
        val clBtnCheckout = binding.clBtnCheckout
        onCountChange()

        clBtnCheckout.setOnClickListener{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }
}