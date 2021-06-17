package com.example.ejercicio3.ui.main.plate

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ejercicio3.R
import com.example.ejercicio3.databinding.ActivityPlateBinding
import com.example.ejercicio3.data.entities.Plate
import com.example.ejercicio3.data.entities.User
import com.example.ejercicio3.data.local.SharedPreferencesManager
import com.example.ejercicio3.data.network.ApiClient
import com.example.ejercicio3.data.network.responses.ExtendedIngredient
import com.example.ejercicio3.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlateActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPlateBinding
    private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager
    var user : User? = null
    companion object{
        const val PLATE_KEY = "platekey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = sharedPrefManager.getUser(this)!!
        val tvErrorMessage = binding.tvErrorMessage
        val plate = intent.extras!!.getInt(PLATE_KEY)
        getPlateDetails(tvErrorMessage, plate)
    }

    //Color y Texto de appbar
    fun setAppBar(sourceName : String){
        val toolbar = binding.appBar.tvToolbar
        toolbar.text = sourceName
        toolbar.setTextColor(this.getColorStateList(R.color.textPrimary))

        setSupportActionBar(binding.appBar.toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    //Cargar datos a la vista
    fun bindData(plate : Plate){
        val ivPlatePhotoDetail = binding.ivPlatePhotoDetail
        val tvPlateNameDetail = binding.tvPlateNameDetail
        val tvPlateDescriptionDetail = binding.tvPlateDescriptionDetail
        val tvPlatePriceDetail = binding.tvPlatePriceDetail
        val tvPlateRaiting = binding.tvPlateRaiting
        val tvIngredients = binding.tvIngredients
        val vPlateFavouriteCard = binding.vPlateFavouriteCard
        val llGlutenFree = binding.llGlutenFree
        val llDairyFree = binding.llDairyFree

        Glide.with(ivPlatePhotoDetail.context).load(plate.image).into(ivPlatePhotoDetail)

        tvPlateNameDetail.text = plate.title
        tvPlateDescriptionDetail.text = if(plate.cuisines.isNullOrEmpty()) "-"
            else plate.cuisines.reduce { acc, string -> "$acc, $string" }
        tvPlatePriceDetail.text = "\$${plate.pricePerServing.toString()}"
        tvPlateRaiting.text = plate.spoonacularScore.toString()

        tvIngredients.text = listToString(plate.extendedIngredients)

        if(plate.glutenFree) llGlutenFree.visibility =
            View.VISIBLE else llGlutenFree.visibility = View.GONE
        if(plate.dairyFree) llDairyFree.visibility =
            View.VISIBLE else llDairyFree.visibility = View.GONE

        val btnBuy = binding.btnBuy
        btnBuy.setOnClickListener {
            MainActivity.shopBox.addPlate(plate)
            goToShopBox()
        }

        vPlateFavouriteCard.background =
            if(user!!.plateIsFav(plate)) this.getDrawable(
                R.drawable.layerlist_favourite_on)
            else this.getDrawable(R.drawable.layerlist_favourite)

        vPlateFavouriteCard.setOnClickListener{
            this.onClickFavPlate(vPlateFavouriteCard, plate)
        }
    }

    fun goToShopBox(){
        val i = Intent(this@PlateActivity, MainActivity::class.java)
        startActivity(i)
    }

    fun onClickFavPlate(view : View, plate : Plate){
        if(user!!.plateIsFav(plate)) {
            user!!.removeToFav(plate)
            view.background =
                this.getDrawable(R.drawable.layerlist_favourite)
        }
        else {
            user!!.addToFav(plate)
            view.background = this.getDrawable(R.drawable.layerlist_favourite_on)
        }
        sharedPrefManager.saveUser(this, user!!)
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
            .enqueue(object: Callback<Plate> {
                override fun onFailure(call : Call<Plate>, t: Throwable){
                    //log
                    t.printStackTrace()
                    tvErr.visibility = View.VISIBLE
                }

                override fun onResponse(call : Call<Plate>,
                                        response : Response<Plate>){
                    if(response.isSuccessful){
                        if(response.code() != 200){
                            tvErr.text = getString(R.string.e500)
                            tvErr.visibility = View.VISIBLE
                        }
                        else{
                            response.body()?.let{
                                tvErr.visibility= View.GONE

                                setAppBar(it.sourceName!!)
                                bindData(it)
                            }
                        }
                    }
                }
            })
    }
}