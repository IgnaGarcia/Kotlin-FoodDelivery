package com.example.ejercicio3.ui.main.searchFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejercicio3.R
import com.example.ejercicio3.ui.main.plateList.PlatesCardAdapter
import com.example.ejercicio3.databinding.ActivitySearchBinding
import com.example.ejercicio3.data.entities.Plate
import com.example.ejercicio3.data.local.SharedPreferencesManager
import com.example.ejercicio3.data.network.ApiClient
import com.example.ejercicio3.data.network.responses.PlateListResponse
import com.example.ejercicio3.ui.main.plate.PlateActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment(), PlatesCardAdapter.OnClickPlate
    , SearchView.OnQueryTextListener {
    private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager
    var plateCardAdapter : PlatesCardAdapter? = null
    private var _binding : ActivitySearchBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?)
            : View? {
        _binding = ActivitySearchBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAppBar()

        val inputSearch = binding.inputSearch
        inputSearch.setOnSearchClickListener{
            onQueryTextSubmit(inputSearch.query.toString())
        }
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
    private fun getPlates(queryString : String){
        val rvPlatesCards = binding.rvPlatesCards
        val progressBar = binding.progressBar
        val tvError = binding.tvError

        ApiClient.getServiceClient().getPlates(queryString)
                .enqueue(object: Callback<PlateListResponse> {
                    override fun onResponse(call: Call<PlateListResponse>,
                                            response: Response<PlateListResponse>) {
                        if(response.isSuccessful){
                            if(response.code() != 200){
                                tvError.text = getString(R.string.e500)
                                progressBar.visibility = View.GONE
                                tvError.visibility = View.VISIBLE
                            }
                            else{
                                response.body()?.let{
                                    if(it.results.isEmpty()){
                                        progressBar.visibility = View.GONE
                                        tvError.text = activity!!.getString(R.string.e404)
                                        tvError.visibility = View.VISIBLE
                                    }
                                    else{
                                        progressBar.visibility = View.GONE
                                        rvPlatesCards.visibility = View.VISIBLE
                                        setPlatesAdapter(rvPlatesCards, it.results)
                                    }
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<PlateListResponse>, t: Throwable) {
                        t.printStackTrace()
                        progressBar.visibility = View.GONE
                        tvError.text = activity!!.getString(R.string.e500)
                        tvError.visibility = View.VISIBLE
                    }
                })
    }

    fun setAppBar(){
        val toolbar = binding.tvToolbar
        toolbar.text = this.getString(R.string.search)
        toolbar.setTextColor(activity!!.getColorStateList(R.color.textPrimary))
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null) getPlates(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean = false
}