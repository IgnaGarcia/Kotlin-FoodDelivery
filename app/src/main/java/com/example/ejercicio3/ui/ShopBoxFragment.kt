package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.ejercicio3.R
import com.example.ejercicio3.adapters.ShopBoxAdapter
import com.example.ejercicio3.adapters.ViewPagerAdapter
import com.example.ejercicio3.databinding.ActivityShopboxBinding
import com.example.ejercicio3.entities.Plate
import com.example.ejercicio3.entities.ShopBox
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.local.SharedPreferencesManager
import com.example.ejercicio3.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopBoxFragment : Fragment(), ShopBoxAdapter.OnClickPlate, ShopBox.OnCountChange {
    private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager
    var user : User? = null
    var shopBoxAdapter : ShopBoxAdapter? = null
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
        user = sharedPrefManager.getUser(activity!!)!!
        MainActivity.shopBox.onCountChange = this

        setBottomText()
        getPlateDetails(binding.tvErrorMessage, 652417)
        setUpTabs()

        binding.clReturnBtn.setOnClickListener{
            val i = Intent(activity, MainActivity::class.java)
            startActivity(i)
        }
    }

    //Intent a partir del click en una tarjeta del RecyclerView
    override fun onClickPlate(plate : Int){
        val i = Intent(activity, PlateActivity::class.java)
        i.putExtra(PlateActivity.PLATE_KEY, plate)
        startActivity(i)
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
                        response.body()?.let{
                            progressBar.visibility = View.GONE
                            setHeader(it)
                        }
                    }
                }
            })
    }

    fun setUpTabs(){
        val adapter = ViewPagerAdapter(childFragmentManager)
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        adapter.addFragment(CeliacoTabFragment(), "Celiaco")
        adapter.addFragment(CetoTabFragment(), "Cetogenico")
        adapter.addFragment(VeganTabFragment(), "Vegano")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    fun setHeader(plate : Plate){
        val ivPlatePhotoDetail = binding.ivPlatePhotoDetail
        val tvRestoTitle = binding.tvRestoTitle
        val tvRestoStars = binding.tvRestoStars
        val llRestoVerified = binding.llRestoVerified
        val vPlateFavouriteCard = binding.vPlateFavouriteCard

        ivPlatePhotoDetail.visibility = View.VISIBLE
        Glide.with(ivPlatePhotoDetail.context).load(plate.image).into(ivPlatePhotoDetail)

        tvRestoTitle.text = plate.title
        tvRestoStars.text = plate.spoonacularScore.toString()
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
        sharedPrefManager.saveUser(activity!!, user!!)
    }

    override fun onCountChange(){
        val tvItemCount = binding.tvItemCount
        val tvTotalPrice = binding.tvTotalPrice

        tvItemCount.text = String.format("%d items", MainActivity.shopBox.plateList.size)
        tvTotalPrice.text = String.format("$%.2f", MainActivity.shopBox.calcTotal())
    }

    fun setBottomText(){
        val llBtnCheckout = binding.llBtnCheckout
        onCountChange()

        llBtnCheckout.setOnClickListener{
            val i = Intent(activity, CheckoutActivity::class.java)
            startActivity(i)
        }
    }
}