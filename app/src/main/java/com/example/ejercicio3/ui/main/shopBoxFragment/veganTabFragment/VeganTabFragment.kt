package com.example.ejercicio3.ui.main.shopBoxFragment.veganTabFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ejercicio3.R
import com.example.ejercicio3.ui.main.shopBoxFragment.PlatesShopCardAdapter
import com.example.ejercicio3.databinding.FragmentTabLayoutBinding
import com.example.ejercicio3.data.entities.Plate
import com.example.ejercicio3.data.network.ApiClient
import com.example.ejercicio3.data.network.responses.PlateListResponse
import com.example.ejercicio3.ui.main.plate.PlateActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VeganTabFragment : Fragment(), PlatesShopCardAdapter.OnClickPlate {
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
        getPlatesByDiet()
    }

    //Pasarle al RecyclerView los datos
    fun setPlatesAdapter(rvPlatesCards : RecyclerView, plates : List<Plate>){
        platesShopCardAdapter = PlatesShopCardAdapter(plates, this, 1)

        rvPlatesCards.adapter = platesShopCardAdapter
        rvPlatesCards.layoutManager = LinearLayoutManager(
            activity!!, LinearLayoutManager.VERTICAL, false)
    }

    //Traer los datos de la API
    private fun getPlatesByDiet(){
        val rvPlatesCards = binding.rvPlatesShopCards
        val progressBar = binding.progressBar
        val tvError = binding.tvFragError

        ApiClient.getServiceClient().getPlatesByDiet("vegan")
            .enqueue(object: Callback<PlateListResponse> {
                override fun onResponse(call: Call<PlateListResponse>,
                                        response: Response<PlateListResponse>
                ) {
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

    override fun onClickPlate(plate: Int) {
        val i = Intent(activity!!, PlateActivity::class.java)
        i.putExtra(PlateActivity.PLATE_KEY, plate)
        startActivity(i)
    }
}