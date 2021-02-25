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
import com.example.ejercicio3.entities.Plate
import com.example.ejercicio3.network.responses.PlateResponse

class ShopBoxAdapter(var plates : MutableList<PlateResponse>, var onClickPlate : ShopBoxAdapter.OnClickPlate)
    : RecyclerView.Adapter<ShopBoxAdapter.BaseViewHolder>(){

    inner class BaseViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val ivPlatePhoto = view.findViewById<ImageView>(R.id.ivPlatePhotoCard)
        val vPlateFavourite = view.findViewById<View>(R.id.vPlateFavouriteCard)
        val tvPlateName = view.findViewById<TextView>(R.id.tvPlateNameCard)
        val tvPlatePrice = view.findViewById<TextView>(R.id.tvPlatePriceCard)
        val tvPlateSINTACC = view.findViewById<TextView>(R.id.tvPlateSINTACCCard)
        val llPopular = view.findViewById<LinearLayout>(R.id.llPopular)


        fun onBind(plate : PlateResponse){
            Glide.with(ivPlatePhoto.context).load(plate.image).centerCrop().into(ivPlatePhoto)
            tvPlateName.text = plate.title
            tvPlatePrice.text = "\$${plate.pricePerServing.toString()}"
            tvPlateSINTACC.text = if(plate.glutenFree) itemView.context.getString(R.string.glutenFree) else ""

            vPlateFavourite.visibility = View.GONE
            llPopular.visibility = View.GONE

            itemView.setOnClickListener{
                onClickPlate.onClickPlate(plate.id)
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