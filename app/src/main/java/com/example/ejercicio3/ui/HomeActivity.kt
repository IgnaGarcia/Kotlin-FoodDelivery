package com.example.ejercicio3.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejercicio3.R
import com.example.ejercicio3.adapters.CategorieAdapter
import com.example.ejercicio3.adapters.PlatesBigCardAdapter
import com.example.ejercicio3.entities.Plate
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.entities.getCategories
import com.example.ejercicio3.local.SharedPreferencesManager
import com.example.ejercicio3.network.ApiClient
import com.example.ejercicio3.network.responses.PlateListResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : Fragment(), PlatesBigCardAdapter.OnClickPlate, CategorieAdapter.OnClickCategorie {
    private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager
    var categorieAdapter : CategorieAdapter? = null
    var plateAdapter : PlatesBigCardAdapter? = null

    companion object {
        fun newInstance(): HomeActivity = HomeActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.activity_home, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUserData()
        chargeCategories()
        chargePlates()

        val btnSeeMore = activity!!.findViewById<TextView>(R.id.btnSeeMore)
        btnSeeMore.setOnClickListener { goToPlatesListActivity(1) }
    }

    //Intent a partir del click en una tarjeta del RecyclerView
    override fun onClickPlate(plateId : Int){
        val i = Intent(activity, PlateActivity::class.java)
        i.putExtra(PlateActivity.PLATE_KEY, plateId)
        startActivity(i)
    }

    //Intent a partir del click en una tarjeta del RecyclerView
    override fun onClickCategorie(categorieId : Int){
        val i = Intent(activity, PlatesListActivity::class.java)
        i.putExtra(PlatesListActivity.QUERY_ID, categorieId)
        startActivity(i)
    }

    //Intent a partir del click en "Ver más"
    fun goToPlatesListActivity(queryId : Int){
        val i = Intent(activity, PlatesListActivity::class.java)
        i.putExtra(PlatesListActivity.QUERY_ID, queryId)
        startActivity(i)
    }

    //Pasarle al RecyclerView los datos
    fun setPlatesAdapter(rvPlatesBig : RecyclerView, plates : List<Plate>){
        plateAdapter = PlatesBigCardAdapter(plates, this)

        rvPlatesBig.adapter = plateAdapter
        rvPlatesBig.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL, false)
    }

    //Traer los datos de la API
    private fun getPlates(rvPlatesBig : RecyclerView, tvErrorMessage : TextView){
        ApiClient.getServiceClient().getPlates(5)
            .enqueue(object: Callback<PlateListResponse> {
                override fun onResponse(call : Call<PlateListResponse>,
                                        response : Response<PlateListResponse>) {
                    if(response.isSuccessful){
                        rvPlatesBig.visibility = View.VISIBLE
                        tvErrorMessage.visibility = View.GONE
                        response.body()?.let{
                            setPlatesAdapter(rvPlatesBig, it.results)
                        }
                    }
                }

                override fun onFailure(call: Call<PlateListResponse>, t: Throwable) {
                    t.printStackTrace()

                    rvPlatesBig.visibility = View.GONE
                    tvErrorMessage.visibility = View.VISIBLE
                }
            })
    }

    //Llamado a getPlates
    fun chargePlates(){
        val rvPlatesBig = activity?.findViewById<RecyclerView>(R.id.rvPlatesBig)
        val tvErrorMessage = activity?.findViewById<TextView>(R.id.tvErrorMessage)
        getPlates(rvPlatesBig!!, tvErrorMessage!!)
    }

    //Bindear los datos del usuario
    fun bindUserData(){
        val user : User = sharedPrefManager.getUser(activity!!)!!
        val tvToolbar = activity?.findViewById<TextView>(R.id.tvToolbar)
        tvToolbar!!.text = user.location

        val tvGreeting = activity?.findViewById<TextView>(R.id.tvGreeting)
        tvGreeting!!.text = "Hola, ${user.username}"
    }

    //Traer lista de categorias y bindear con RecyclerView
    fun chargeCategories(){
        val categorieList = getCategories(activity!!)
        val rvCategories = activity!!.findViewById<RecyclerView>(R.id.rvCategories)
        categorieAdapter = CategorieAdapter(categorieList, this)

        rvCategories.adapter = categorieAdapter
        rvCategories.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL, false)
    }
}
