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
import com.example.ejercicio3.entities.Plate
import com.example.ejercicio3.network.ApiClient
import com.example.ejercicio3.network.responses.PlateListResponse
import com.example.ejercicio3.network.responses.PlateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment(), PlatesCardAdapter.OnClickPlate, SearchView.OnQueryTextListener {
    var plateCardAdapter : PlatesCardAdapter? = null

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.activity_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAppBar()
        val inputSearch = activity!!.findViewById<SearchView>(R.id.inputSearch)
        inputSearch.setOnQueryTextListener(this)
    }

    //Intent a partir del click en una tarjeta del RecyclerView
    override fun onClickPlate(plate : Int){
        val i = Intent(activity, PlateActivity::class.java)
        i.putExtra(PlateActivity.PLATE_KEY, plate)
        startActivity(i)
    }

    //Pasarle al RecyclerView los datos
    fun setPlatesAdapter(rvPlatesCards : RecyclerView, plates : List<Plate>){
        plateCardAdapter = PlatesCardAdapter(plates, this)

        rvPlatesCards.adapter = plateCardAdapter
        rvPlatesCards.layoutManager = LinearLayoutManager(
                activity, LinearLayoutManager.VERTICAL, false)
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

    fun setAppBar(){
        val toolbar = activity!!.findViewById<TextView>(R.id.tvToolbar)
        toolbar.text = this.getString(R.string.search)
        toolbar.setTextColor(activity!!.getColorStateList(R.color.textPrimary))
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val rvPlatesCards = activity!!.findViewById<RecyclerView>(R.id.rvPlatesCards)
        val tvErrorMessage = activity!!.findViewById<TextView>(R.id.tvErrorMessage)
        if(query != null) getPlates(rvPlatesCards, tvErrorMessage, query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean = false
}