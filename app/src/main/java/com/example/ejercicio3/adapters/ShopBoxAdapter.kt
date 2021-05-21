package com.example.ejercicio3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ejercicio3.R
import com.example.ejercicio3.databinding.ItemPlatesCardBinding
import com.example.ejercicio3.entities.Plate
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.local.SharedPreferencesManager
import com.example.ejercicio3.network.responses.PlateResponse

class ShopBoxAdapter(var plates : MutableList<Plate>,
                     var onClickPlate : ShopBoxAdapter.OnClickPlate)
    : RecyclerView.Adapter<ShopBoxAdapter.BaseViewHolder>(){

    inner class BaseViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager
        val user : User = sharedPrefManager.getUser(itemView.context)!!
        val binding = ItemPlatesCardBinding.bind(view)
        val ivPlatePhoto = binding.ivPlatePhotoCard
        val vPlateFavourite = binding.vPlateFavouriteCard
        val tvPlateName = binding.tvPlateNameCard
        val tvPlatePrice = binding.tvPlatePriceCard
        val tvPlateSINTACC = binding.tvPlateSINTACCCard
        val llPopular = binding.llPopular


        fun onBind(plate : Plate){
            Glide.with(ivPlatePhoto.context).load(plate.image).centerCrop().into(ivPlatePhoto)
            tvPlateName.text = plate.title
            tvPlatePrice.text = "\$${plate.pricePerServing.toString()}"
            tvPlateSINTACC.text = if(plate.glutenFree) itemView.context.getString(R.string.glutenFree) else ""

            vPlateFavourite.background =
                if(user.favourites.contains(plate)) itemView.context.getDrawable(
                    R.drawable.layerlist_favourite_on)
                else itemView.context.getDrawable(R.drawable.layerlist_favourite)
            llPopular.visibility = View.GONE

            itemView.setOnClickListener{
                onClickPlate.onClickPlate(plate.id)
            }

            vPlateFavourite.setOnClickListener{
                if(user.favourites.contains(plate)) {
                    user.removeToFav(plate)
                    vPlateFavourite.background =
                        itemView.context.getDrawable(R.drawable.layerlist_favourite)
                }
                else {
                    user.addToFav(plate)
                    vPlateFavourite.background = itemView.context.getDrawable(
                        R.drawable.layerlist_favourite_on)
                }
                sharedPrefManager.saveUser(itemView.context, user)
            }
        }
    }

    interface OnClickPlate{
        fun onClickPlate(plate : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ShopBoxAdapter.BaseViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_plates_card, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopBoxAdapter.BaseViewHolder, position: Int) {
        holder.onBind(plates[position])
    }

    override fun getItemCount(): Int = plates.size
}