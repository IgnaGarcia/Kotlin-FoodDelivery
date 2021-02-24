package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejercicio3.R
import com.example.ejercicio3.adapters.PlatesCardAdapter
import com.example.ejercicio3.entities.Plate
import com.example.ejercicio3.network.ApiClient
import com.example.ejercicio3.network.responses.PlateListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlatesListActivity : AppCompatActivity(), PlatesCardAdapter.OnClickPlate  {
    var plateCardAdapter : PlatesCardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plates_list)

        setAppBar()
        chargePlates()
    }

    //Intent a partir del click en una tarjeta del RecyclerView
    override fun onClickPlate(plate : Int){
        val i = Intent(this@PlatesListActivity, PlateActivity::class.java)
        i.putExtra(PlateActivity.PLATE_KEY, plate)
        startActivity(i)
    }

    //Pasarle al RecyclerView los datos
    fun setPlatesAdapter(rvPlatesCards : RecyclerView, plates : List<Plate>){
        plateCardAdapter = PlatesCardAdapter(plates, this)

        rvPlatesCards.adapter = plateCardAdapter
        rvPlatesCards.layoutManager = LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false)
    }

    //Traer los datos de la API
    private fun getMorePlates(rvPlatesCards : RecyclerView, tvErrorMessage : TextView){
        ApiClient.getServiceClient().getMorePlates()
                .enqueue(object: Callback<PlateListResponse> {
                    override fun onResponse(call: Call<PlateListResponse>, response: Response<PlateListResponse>) {
                        if(response.isSuccessful){
                            rvPlatesCards.visibility = View.VISIBLE
                            tvErrorMessage.visibility = View.GONE
                            response.body()?.let{
                                setPlatesAdapter(rvPlatesCards, it.results)
                            }
                        }
                    }

                    override fun onFailure(call: Call<PlateListResponse>, t: Throwable) {
                        t.printStackTrace()

                        rvPlatesCards.visibility = View.GONE
                        tvErrorMessage.visibility = View.VISIBLE
                    }
                })
    }

    //Llamado a getMorePlates
    fun chargePlates(){
        val rvPlatesCards = findViewById<RecyclerView>(R.id.rvPlatesCards)
        val tvErrorMessage = findViewById<TextView>(R.id.tvErrorMessage)
        getMorePlates(rvPlatesCards, tvErrorMessage)
    }

    //Setear texto y color al AppBar
    fun setAppBar(){
        val toolbar = findViewById<TextView>(R.id.tvToolbar)
        toolbar.text = this.getString(R.string.promotion)
        toolbar.setTextColor(this.getColorStateList(R.color.textPrimary))

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}