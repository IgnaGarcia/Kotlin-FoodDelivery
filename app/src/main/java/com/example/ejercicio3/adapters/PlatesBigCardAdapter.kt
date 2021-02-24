package com.example.ejercicio3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ejercicio3.R
import com.example.ejercicio3.entities.Plate

class PlatesBigCardAdapter(var plates : List<Plate>, var onClickPlate : OnClickPlate) : RecyclerView.Adapter<PlatesBigCardAdapter.BaseViewHolder>(){

    inner class BaseViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val ivPlatePhoto = view.findViewById<ImageView>(R.id.ivPlatePhoto)
        val vPlateFavourite = view.findViewById<View>(R.id.vPlateFavourite)
        val tvPlateName = view.findViewById<TextView>(R.id.tvPlateName)
        val tvPlateDescription = view.findViewById<TextView>(R.id.tvPlateDescription)
        val tvPlatePrice = view.findViewById<TextView>(R.id.tvPlatePrice)
        val tvPlateSINTACC = view.findViewById<TextView>(R.id.tvPlateSINTACC)


        fun onBind(plate : Plate){
            Glide.with(ivPlatePhoto.context).load(plate.image).centerCrop().into(ivPlatePhoto)
            vPlateFavourite.background = if(plate.isFavourite) itemView.context.getDrawable(R.drawable.layerlist_favourite_on)
                                       else itemView.context.getDrawable(R.drawable.layerlist_favourite)
            tvPlateName.text = plate.title
            tvPlateDescription.text = if(plate.cuisines.isNullOrEmpty()) "-" else plate.cuisines.reduce { acc, string -> "$acc, $string" }
            tvPlatePrice.text = "\$${plate.pricePerServing.toString()}"
            tvPlateSINTACC.text = if(plate.glutenFree) itemView.context.getString(R.string.glutenFree) else ""

            itemView.setOnClickListener{
                onClickPlate.onClickPlate(plate.id)
            }

        }
    }

    interface OnClickPlate{
        fun onClickPlate(plate : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatesBigCardAdapter.BaseViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_plates_big, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlatesBigCardAdapter.BaseViewHolder, position: Int) {
        holder.onBind(plates[position])
    }

    override fun getItemCount(): Int = plates.size
}