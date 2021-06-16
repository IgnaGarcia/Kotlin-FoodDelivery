package com.example.ejercicio3.ui.main.homeFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ejercicio3.R
import com.example.ejercicio3.databinding.ItemPlatesBigBinding
import com.example.ejercicio3.data.entities.Plate
import com.example.ejercicio3.data.entities.User
import com.example.ejercicio3.data.local.SharedPreferencesManager

class PlatesBigCardAdapter(var plates : List<Plate>, var onClickPlate : OnClickPlate)
    : RecyclerView.Adapter<PlatesBigCardAdapter.BaseViewHolder>(){

    inner class BaseViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager
        val user : User = sharedPrefManager.getUser(itemView.context)!!
        val binding = ItemPlatesBigBinding.bind(view)
        val ivPlatePhoto = binding.ivPlatePhoto
        val vPlateFavourite = binding.vPlateFavourite
        val tvPlateName = binding.tvPlateName
        val tvPlateDescription = binding.tvPlateDescription
        val tvPlatePrice = binding.tvPlatePrice
        val tvPlateSINTACC = binding.tvPlateSINTACC


        fun onBind(plate : Plate){
            Glide.with(ivPlatePhoto.context).load(plate.image).centerCrop().into(ivPlatePhoto)
            vPlateFavourite.background =
                if(user.plateIsFav(plate)) itemView.context.getDrawable(
                    R.drawable.layerlist_favourite_on)
                else itemView.context.getDrawable(R.drawable.layerlist_favourite)
            tvPlateName.text = plate.title
            tvPlateDescription.text =
                if(plate.cuisines.isNullOrEmpty()) "-"
                else plate.cuisines.reduce { acc, string -> "$acc, $string" }
            tvPlatePrice.text = "\$${plate.pricePerServing.toString()}"
            tvPlateSINTACC.text = if(plate.glutenFree) itemView.context.getString(R.string.glutenFree) else ""

            itemView.setOnClickListener{
                onClickPlate.onClickPlate(plate.id)
            }

            vPlateFavourite.setOnClickListener{
                if(user.plateIsFav(plate)) {
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
            : BaseViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_plates_big, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(plates[position])
    }

    override fun getItemCount(): Int = plates.size
}