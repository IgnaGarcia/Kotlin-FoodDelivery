package com.example.ejercicio3.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ejercicio3.R
import com.example.ejercicio3.network.ApiClient
import com.example.ejercicio3.network.responses.ExtendedIngredient
import com.example.ejercicio3.network.responses.PlateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlateActivity : AppCompatActivity() {
    companion object{
        const val PLATE_KEY = "platekey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plate)

        val tvErrorMessage = findViewById<TextView>(R.id.tvErrorMessage)
        val plate = intent.extras!!.getInt(PLATE_KEY)
        getPlateDetails(tvErrorMessage, plate)
    }

    //Color y Texto de appbar
    fun setAppBar(sourceName : String){
        val toolbar = findViewById<TextView>(R.id.tvToolbar)
        toolbar.text = sourceName
        toolbar.setTextColor(this.getColorStateList(R.color.textPrimary))

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    //Cargar datos a la vista
    fun bindData(plate : PlateResponse){
        val ivPlatePhotoDetail = findViewById<ImageView>(R.id.ivPlatePhotoDetail)
        val tvPlateNameDetail = findViewById<TextView>(R.id.tvPlateNameDetail)
        val tvPlateDescriptionDetail = findViewById<TextView>(R.id.tvPlateDescriptionDetail)
        val tvPlatePriceDetail = findViewById<TextView>(R.id.tvPlatePriceDetail)
        val tvPlateRaiting = findViewById<TextView>(R.id.tvPlateRaiting)
        val tvIngredients = findViewById<TextView>(R.id.tvIngredients)
        val llGlutenFree = findViewById<LinearLayout>(R.id.llGlutenFree)
        val llDairyFree = findViewById<LinearLayout>(R.id.llDairyFree)

        Glide.with(ivPlatePhotoDetail.context).load(plate.image).into(ivPlatePhotoDetail)

        tvPlateNameDetail.text = plate.title
        tvPlateDescriptionDetail.text = if(plate.cuisines.isNullOrEmpty()) "-" else plate.cuisines.reduce { acc, string -> "$acc, $string" }
        tvPlatePriceDetail.text = "\$${plate.pricePerServing.toString()}"
        tvPlateRaiting.text = plate.spoonacularScore.toString()

        tvIngredients.text = listToString(plate.extendedIngredients)

        if(plate.glutenFree) llGlutenFree.visibility = View.VISIBLE else llGlutenFree.visibility = View.GONE
        if(plate.dairyFree) llDairyFree.visibility = View.VISIBLE else llDairyFree.visibility = View.GONE
    }

    //Convertir la lista de ingredientes en un string
    fun listToString(ingredients : List<ExtendedIngredient>) : String{
        var ingrList : String = "Ingredientes:"
        for(ingr in ingredients) ingrList = "$ingrList\n- ${ingr.originalName}"
        return ingrList
    }

    //Traer datos de la API
    private fun getPlateDetails(tvErr : TextView, plate : Int){
        ApiClient.getServiceClient().getPlateDetails(plate)
            .enqueue(object: Callback<PlateResponse> {
                override fun onFailure(call : Call<PlateResponse>, t: Throwable){
                    //log
                    t.printStackTrace()
                    tvErr.visibility = View.VISIBLE
                }

                override fun onResponse(call : Call<PlateResponse>, response : Response<PlateResponse>){
                    if(response.isSuccessful){
                        response.body()?.let{
                            tvErr.visibility= View.GONE

                            setAppBar(it.sourceName)
                            bindData(it)
                        }
                    }
                }
            })
    }
}