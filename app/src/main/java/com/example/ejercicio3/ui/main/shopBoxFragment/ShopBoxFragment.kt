package com.example.ejercicio3.ui.main.shopBoxFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.ejercicio3.R
import com.example.ejercicio3.databinding.ActivityShopboxBinding
import com.example.ejercicio3.data.entities.Plate
import com.example.ejercicio3.data.entities.ShopBox
import com.example.ejercicio3.data.entities.User
import com.example.ejercicio3.data.local.SharedPreferencesManager
import com.example.ejercicio3.data.network.ApiClient
import com.example.ejercicio3.ui.main.MainActivity
import com.example.ejercicio3.ui.main.checkout.CheckoutActivity
import com.example.ejercicio3.ui.main.plate.PlateActivity
import com.example.ejercicio3.ui.main.shopBoxFragment.celiacoTabFragment.CeliacoTabFragment
import com.example.ejercicio3.ui.main.shopBoxFragment.cetoTabFragment.CetoTabFragment
import com.example.ejercicio3.ui.main.shopBoxFragment.veganTabFragment.VeganTabFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopBoxFragment : Fragment(), ShopBox.OnCountChange {
    var user : User? = null
    private var _binding : ActivityShopboxBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): ShopBoxFragment = ShopBoxFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?)
            : View {
        _binding = ActivityShopboxBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = SharedPreferencesManager.getUser(activity!!)!!
        MainActivity.shopBox.onCountChange = this

        setBottomText()
        getPlateDetails(binding.tvErrorMessage, 652417)
        setUpTabs()
        setAppBar()
    }

    fun setAppBar(){
        binding.tvToolbar.text = activity!!.getString(R.string.myCart)
        binding.vBackBtn.setOnClickListener{
            startActivity(Intent(activity!!, MainActivity::class.java))
        }
    }

    //Traer datos de la API
    private fun getPlateDetails(tvErr : TextView, plate : Int){
        val progressBar = binding.progressBar

        ApiClient.getServiceClient().getPlateDetails(plate)
            .enqueue(object: Callback<Plate> {
                override fun onFailure(call : Call<Plate>, t: Throwable){
                    t.printStackTrace()
                    progressBar.visibility = View.GONE
                    tvErr.visibility = View.VISIBLE
                }

                override fun onResponse(call : Call<Plate>,
                                        response : Response<Plate>){
                    if(response.isSuccessful){
                        if(response.code() != 200){
                            tvErr.text = getString(R.string.e500)
                            progressBar.visibility = View.GONE
                            tvErr.visibility = View.VISIBLE
                        }
                        else {
                            response.body()?.let {
                                progressBar.visibility = View.GONE
                                setHeader(it)
                            }
                        }
                    }
                }
            })
    }

    fun setUpTabs(){
        val adapter = ViewPagerAdapter(childFragmentManager)
        val viewPager = binding.viewPager

        adapter.addFragment(CeliacoTabFragment(), "Celiaco")
        adapter.addFragment(CetoTabFragment(), "Cetogenico")
        adapter.addFragment(VeganTabFragment(), "Vegano")

        viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(viewPager)
    }

    fun setHeader(plate : Plate){
        val ivPlatePhotoDetail = binding.ivPlatePhotoDetail
        val llRestoVerified = binding.llRestoVerified
        val vPlateFavouriteCard = binding.vPlateFavouriteCard
        vPlateFavouriteCard.visibility = View.VISIBLE

        ivPlatePhotoDetail.visibility = View.VISIBLE
        Glide.with(ivPlatePhotoDetail.context).load(plate.image).into(ivPlatePhotoDetail)

        binding.tvRestoTitle.text = plate.title
        binding.tvRestoStars.text = plate.spoonacularScore.toString()
        if (plate.spoonacularScore > 80.0) llRestoVerified.visibility = View.VISIBLE
        else llRestoVerified.visibility = View.GONE

        vPlateFavouriteCard.background =
            if(user!!.plateIsFav(plate)) activity!!.getDrawable(
                R.drawable.layerlist_favourite_on)
            else activity!!.getDrawable(R.drawable.layerlist_favourite)

        vPlateFavouriteCard.setOnClickListener{
            this.onClickFavPlate(vPlateFavouriteCard, plate)
        }
    }

    fun onClickFavPlate(view : View, plate : Plate){
        if(user!!.plateIsFav(plate)) {
            user!!.removeToFav(plate)
            view.background =
                activity!!.getDrawable(R.drawable.layerlist_favourite)
        }
        else {
            user!!.addToFav(plate)
            view.background = activity!!.getDrawable(R.drawable.layerlist_favourite_on)
        }
        SharedPreferencesManager.saveUser(activity!!, user!!)
    }

    override fun onCountChange(){
        binding.tvItemCount.text = String.format("%d items", MainActivity.shopBox.plateList.size)
        binding.tvTotalPrice.text = String.format("$%.2f", MainActivity.shopBox.calcTotal())
    }

    fun setBottomText(){
        onCountChange()
        binding.llBtnCheckout.setOnClickListener{
            startActivity(Intent(activity, CheckoutActivity::class.java))
        }
    }
}