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
import com.example.ejercicio3.databinding.ActivityPlatesListBinding
import com.example.ejercicio3.entities.Plate
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.local.SharedPreferencesManager
import com.example.ejercicio3.network.ApiClient
import com.example.ejercicio3.network.responses.PlateListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlatesListActivity : AppCompatActivity(), PlatesCardAdapter.OnClickPlate{
    var plateCardAdapter : PlatesCardAdapter? = null
    private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager
    private var plates : List<Plate> = listOf()
    private lateinit var binding : ActivityPlatesListBinding

    companion object{
        const val QUERY_ID = "query"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlatesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extra =
                if (intent.extras?.getInt(QUERY_ID) == null) 1
                else intent.extras?.getInt(QUERY_ID)

        setAppBar(extra!!)
        chargePlates(extra!!)
    }

    //Intent a partir del click en una tarjeta del RecyclerView
    override fun onClickPlate(plate : Int){
        val i = Intent(this@PlatesListActivity, PlateActivity::class.java)
        i.putExtra(PlateActivity.PLATE_KEY, plate)
        startActivity(i)
    }

    //Pasarle al RecyclerView los datos
    fun setPlatesAdapter(rvPlatesCards : RecyclerView, tvErrorMessage : TextView, queryId : Int, offset : Int){
        if(plates.size >= 5 || queryId == 1){
            plateCardAdapter = PlatesCardAdapter(plates, this)

            rvPlatesCards.adapter = plateCardAdapter
            rvPlatesCards.layoutManager = LinearLayoutManager(
                    this, LinearLayoutManager.VERTICAL, false)
        }
       else {
            val offst = offset + 20
            getPlates(rvPlatesCards, tvErrorMessage,  queryId, offst)
        }
    }

    //Traer los datos de la API
    private fun getPlates(rvPlatesCards : RecyclerView, tvErrorMessage : TextView, queryId : Int, offset : Int){
        ApiClient.getServiceClient().getPlates(20, offset)
                .enqueue(object: Callback<PlateListResponse> {
                    override fun onResponse(call: Call<PlateListResponse>,
                                            response: Response<PlateListResponse>) {
                        if(response.isSuccessful){
                            rvPlatesCards.visibility = View.VISIBLE
                            tvErrorMessage.visibility = View.GONE
                            response.body()?.let{
                                plates = plates.plus(it.results)
                                when(queryId){
                                    2 -> {
                                        plates = plates.filter { s -> s.pricePerServing < 80 }
                                        setPlatesAdapter(rvPlatesCards, tvErrorMessage, queryId, offset)
                                    }
                                    3 -> {
                                        plates = plates.filter { s -> s.veryPopular }
                                        setPlatesAdapter(rvPlatesCards, tvErrorMessage, queryId, offset)
                                    }
                                    else -> setPlatesAdapter(rvPlatesCards, tvErrorMessage, queryId, offset)
                                }

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
    fun chargePlates(queryId : Int){
        val rvPlatesCards = binding.llPlateList.rvPlatesCards
        val tvErrorMessage = binding.tvErrorMessage
        if (queryId == 1) {
            val user : User = sharedPrefManager.getUser(this)!!
            plates = user.favourites
            setPlatesAdapter(rvPlatesCards, tvErrorMessage, 1, 0)
        } else {
            getPlates(rvPlatesCards, tvErrorMessage, queryId, 0)
        }

    }

    //Setear texto y color al AppBar
    fun setAppBar(queryId : Int){
        val toolbar = binding.appBar.tvToolbar
        when(queryId){
            1 -> toolbar.text = this.getString(R.string.favourites)
            2 -> toolbar.text = this.getString(R.string.offers)
            3 -> toolbar.text = this.getString(R.string.trends)
            else -> toolbar.text = this.getString(R.string.promotion)
        }
        toolbar.setTextColor(this.getColorStateList(R.color.textPrimary))

        setSupportActionBar(binding.appBar.toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}