package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejercicio3.adapters.PlatesShopCardAdapter
import com.example.ejercicio3.databinding.FragmentTabLayoutBinding
import com.example.ejercicio3.entities.Plate
import com.example.ejercicio3.network.ApiClient
import com.example.ejercicio3.network.responses.PlateListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CetoTabFragment : Fragment(), PlatesShopCardAdapter.OnClickPlate {
    private var platesShopCardAdapter : PlatesShopCardAdapter? = null
    private var _binding : FragmentTabLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabLayoutBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chargePlates()
    }

    fun chargePlates(){
        val rvPlatesCards = binding.llPlateList.rvPlatesShopCards
        val tvFragErrorMessage = binding.tvFragErrorMessage
        getPlatesByDiet(rvPlatesCards, tvFragErrorMessage)
    }

    //Pasarle al RecyclerView los datos
    fun setPlatesAdapter(rvPlatesCards : RecyclerView, plates : List<Plate>){
        platesShopCardAdapter = PlatesShopCardAdapter(plates, this, 1)

        rvPlatesCards.adapter = platesShopCardAdapter
        rvPlatesCards.layoutManager = LinearLayoutManager(
            activity!!, LinearLayoutManager.VERTICAL, false)
    }

    //Traer los datos de la API
    private fun getPlatesByDiet(rvPlatesCards : RecyclerView, tvFragErrorMessage : TextView){
        ApiClient.getServiceClient().getPlatesByDiet("ketogenic")
            .enqueue(object: Callback<PlateListResponse> {
                override fun onResponse(call: Call<PlateListResponse>,
                                        response: Response<PlateListResponse>
                ) {
                    if(response.isSuccessful){
                        rvPlatesCards.visibility = View.VISIBLE
                        tvFragErrorMessage.visibility = View.GONE
                        response.body()?.let{
                            setPlatesAdapter(rvPlatesCards, it.results)
                        }
                    }
                }
                override fun onFailure(call: Call<PlateListResponse>, t: Throwable) {
                    t.printStackTrace()
                    rvPlatesCards.visibility = View.GONE
                    tvFragErrorMessage.visibility = View.VISIBLE
                }
            })
    }

    override fun onClickPlate(plate: Int) {
        val i = Intent(activity!!, PlateActivity::class.java)
        i.putExtra(PlateActivity.PLATE_KEY, plate)
        startActivity(i)
    }
}