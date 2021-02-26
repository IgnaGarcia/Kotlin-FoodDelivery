package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejercicio3.R
import com.example.ejercicio3.adapters.PlatesCardAdapter
import com.example.ejercicio3.network.ApiClient
import com.example.ejercicio3.network.responses.PlateListResponse
import com.example.ejercicio3.network.responses.PlateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment(), PlatesCardAdapter.OnClickPlate {
    var platesCardAdapter : PlatesCardAdapter? = null

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.activity_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAppBar()
        chargePlates()
    }

    //Intent a partir del click en una tarjeta del RecyclerView
    override fun onClickPlate(plate : Int){
        val i = Intent(activity, PlateActivity::class.java)
        i.putExtra(PlateActivity.PLATE_KEY, plate)
        startActivity(i)
    }

    //Pasarle al RecyclerView los datos
    fun setPlatesAdapter(rvPlatesCards : RecyclerView){
        plateCardAdapter = PlatesCardAdapter(plates, this)

        rvPlatesCards.adapter = plateCardAdapter
        rvPlatesCards.layoutManager = LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false)
    }

    //Traer los datos de la API
    private fun getPlates(rvPlatesCards : RecyclerView, tvErrorMessage : TextView, queryString : String){
        ApiClient.getServiceClient().getPlates(queryString)
                .enqueue(object: Callback<PlateListResponse> {
                    override fun onResponse(call: Call<PlateListResponse>,
                                            response: Response<PlateListResponse>) {
                        if(response.isSuccessful){
                            rvPlatesCards.visibility = View.VISIBLE
                            tvErrorMessage.visibility = View.GONE
                            response.body()?.let{
                                setPlatesAdapter(rvPlatesCards)
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
        val rvPlatesCards = activity!!.findViewById<RecyclerView>(R.id.rvPlatesCards)
        val tvErrorMessage = activity!!.findViewById<TextView>(R.id.tvErrorMessage)
        val inputSearch = activity!!.findViewById<SearchView>(R.id.inputSearch)


        getPlates(rvPlatesCards, tvErrorMessage, queryString)
    }

    fun setAppBar(){
        val toolbar = activity!!.findViewById<TextView>(R.id.tvToolbar)
        toolbar.text = this.getString(R.string.search)
        toolbar.setTextColor(activity!!.getColorStateList(R.color.textPrimary))
    }
}