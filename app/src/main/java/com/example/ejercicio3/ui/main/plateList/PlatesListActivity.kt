package com.example.ejercicio3.ui.main.plateList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejercicio3.R
import com.example.ejercicio3.databinding.ActivityPlatesListBinding
import com.example.ejercicio3.data.entities.Plate
import com.example.ejercicio3.data.entities.User
import com.example.ejercicio3.data.local.SharedPreferencesManager
import com.example.ejercicio3.data.network.ApiClient
import com.example.ejercicio3.data.network.responses.PlateListResponse
import com.example.ejercicio3.ui.main.plate.PlateActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlatesListActivity : AppCompatActivity(), PlatesCardAdapter.OnClickPlate{
    var plateCardAdapter : PlatesCardAdapter? = null
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
    fun setPlatesAdapter(rvPlatesCards : RecyclerView, queryId : Int, offset : Int){
        if(plates.size >= 5 || queryId == 1){
            plateCardAdapter = PlatesCardAdapter(plates, this)

            rvPlatesCards.adapter = plateCardAdapter
            rvPlatesCards.layoutManager = LinearLayoutManager(
                    this, LinearLayoutManager.VERTICAL, false)
        }
       else {
            getPlates(queryId, offset + 20)
        }
    }

    //Traer los datos de la API
    private fun getPlates(queryId : Int, offset : Int){
        val progressBar = binding.progressBar
        val rvPlatesCards = binding.rvPlatesCards
        val tvError = binding.tvError

        ApiClient.getServiceClient().getPlates(20, offset)
                .enqueue(object: Callback<PlateListResponse> {
                    override fun onResponse(call: Call<PlateListResponse>,
                                            response: Response<PlateListResponse>) {
                        if(response.isSuccessful){
                            if(response.code() != 200){
                                tvError.text = getString(R.string.e500)
                                progressBar.visibility = View.GONE
                                tvError.visibility = View.VISIBLE
                            }
                            else {
                                response.body()?.let{
                                    if(it.results.isEmpty()){
                                        progressBar.visibility = View.GONE
                                        tvError.text = getString(R.string.e404)
                                        tvError.visibility = View.VISIBLE
                                    }
                                    else{
                                        plates = plates.plus(it.results)
                                        when(queryId){
                                            2 -> {
                                                plates = plates.filter {
                                                        s -> s.pricePerServing < 80 }
                                                setPlatesAdapter(rvPlatesCards, queryId, offset)
                                            }
                                            3 -> {
                                                plates = plates.filter { s -> s.veryPopular }
                                                setPlatesAdapter(rvPlatesCards, queryId, offset)
                                            }
                                            else -> setPlatesAdapter(rvPlatesCards, queryId, offset)
                                        }

                                        progressBar.visibility = View.GONE
                                        rvPlatesCards.visibility = View.VISIBLE
                                        setPlatesAdapter(rvPlatesCards, queryId, offset)
                                    }
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<PlateListResponse>, t: Throwable) {
                        t.printStackTrace()
                        progressBar.visibility = View.GONE
                        tvError.text = getString(R.string.e500)
                        tvError.visibility = View.VISIBLE
                    }
                })
    }

    //Llamado a getMorePlates
    fun chargePlates(queryId : Int){
        if (queryId == 1) {
            val user : User = SharedPreferencesManager.getUser(this)!!
            plates = user.favourites
            if(plates.isEmpty()){
                binding.tvError.text = getString(R.string.e404)
                binding.progressBar.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
                setPlatesAdapter(binding.rvPlatesCards, 1, 0)
            }
        } else {
            getPlates(queryId, 0)
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