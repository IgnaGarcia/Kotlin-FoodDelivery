package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
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
        setPlatesAdapter(MainActivity.shopBox.plateList)
        setBottomText()
    }

    fun goToPlateList(){
        val i = Intent(this, PlatesListActivity::class.java)
        i.putExtra(PlatesListActivity.QUERY_ID, 4)
        startActivity(i)
    }

    fun setPlatesAdapter(plates : List<Plate>){
        val rvPlatesCards = binding.llPlateList.rvPlatesShopCards
        val progressBar = binding.progressBar
        val tvError = binding.tvError

        if(plates.isEmpty()){
            tvError.text = getString(R.string.e404)
            progressBar.visibility = View.GONE
            tvError.visibility = View.VISIBLE
        } else {
            platesShopCardAdapter = PlatesShopCardAdapter(plates, this, 2)

            rvPlatesCards.adapter = platesShopCardAdapter
            rvPlatesCards.layoutManager = LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false)

            progressBar.visibility = View.GONE
            rvPlatesCards.visibility = View.VISIBLE
        }
    }

    override fun onClickPlate(plate: Int) {
        val i = Intent(this, PlateActivity::class.java)
        i.putExtra(PlateActivity.PLATE_KEY, plate)
        startActivity(i)
    }

    override fun onCountChange(){
        val total = MainActivity.shopBox.calcTotal()
        binding.tvItemTotalPrice.text = String.format("$%.2f", total)
        binding.tvTotalPrice.text = String.format("$%.2f", total)
        binding.tvFinalPrice.text = String.format("$%.2f", total)
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