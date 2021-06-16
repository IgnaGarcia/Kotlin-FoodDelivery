package com.example.ejercicio3.ui.main.shopBoxFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ejercicio3.R
import com.example.ejercicio3.databinding.ItemPlatesShopCardBinding
import com.example.ejercicio3.data.entities.Plate
import com.example.ejercicio3.data.entities.User
import com.example.ejercicio3.data.local.SharedPreferencesManager
import com.example.ejercicio3.ui.main.MainActivity

class PlatesShopCardAdapter(var plates : List<Plate>, var onClickPlate : OnClickPlate, var type : Int)
    : RecyclerView.Adapter<PlatesShopCardAdapter.BaseViewHolder>(){

    inner class BaseViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager
        val user : User = sharedPrefManager.getUser(itemView.context)!!
        val binding = ItemPlatesShopCardBinding.bind(view)

        val ivPlatePhoto = binding.ivPlatePhotoCard
        val vBtnIcon = binding.vBtnIcon
        val tvPlateName = binding.tvPlateNameCard
        val tvPlatePrice = binding.tvPlatePriceCard
        val llFreeDelivery = binding.llFreeDelivery
        val btnAdd = binding.btnAdd
        val btnRemove = binding.btnRemove
        val tvCount = binding.tvCount
        val tvCountLeft = binding.tvCountLeft


        fun onBind(plate : Plate){
            Glide.with(ivPlatePhoto.context).load(plate.image).centerCrop().into(ivPlatePhoto)
            tvPlateName.text = plate.title
            tvPlatePrice.text = "\$${plate.pricePerServing.toString()}"

            if (plate.pricePerServing > 30)
                llFreeDelivery.visibility = View.VISIBLE
            else llFreeDelivery.visibility = View.GONE

            itemView.setOnClickListener{
                onClickPlate.onClickPlate(plate.id)
            }

            if(MainActivity.shopBox.countMap.containsKey(plate.id)){
                tvCount.text = MainActivity.shopBox.countMap[plate.id].toString()
                if(type == 1) tvCountLeft.text =
                    (5 - MainActivity.shopBox.countMap[plate.id]!!).toString()
            } else {
                tvCount.text = "0"
                if(type == 1) tvCountLeft.text = "5 left"
            }

            btnAdd.setOnClickListener{
                if (tvCount.text.equals("0")) MainActivity.shopBox.addPlate(plate)
                else if (MainActivity.shopBox.countMap[plate.id]!! < 5)
                    MainActivity.shopBox.changeCount(plate, 1)
                tvCount.text = MainActivity.shopBox.countMap[plate.id].toString()
                if(type == 1) tvCountLeft.text =
                    (5 - MainActivity.shopBox.countMap[plate.id]!!).toString() + " left"
            }

            btnRemove.setOnClickListener{
                if ((tvCount.text as String).toInt() > 1) {
                    MainActivity.shopBox.changeCount(plate, 0)
                    tvCount.text = MainActivity.shopBox.countMap[plate.id].toString()
                    if(type == 1) tvCountLeft.text =
                        (5 - MainActivity.shopBox.countMap[plate.id]!!).toString() + " left"
                }
                else if(tvCount.text.equals("1")){
                    MainActivity.shopBox.removePlate(plate)
                    tvCount.text = "0"
                    if(type == 1) tvCountLeft.text = "5 left"
                }
            }

            if (type == 1){
                vBtnIcon.setOnClickListener{ toggleFav(plate) }

                vBtnIcon.background =
                    if(user.plateIsFav(plate)) itemView.context.getDrawable(
                        R.drawable.layerlist_favourite_on)
                    else itemView.context.getDrawable(R.drawable.layerlist_favourite)
            } else if (type == 2) {
                vBtnIcon.setOnClickListener{ removeFromList(plate) }

                vBtnIcon.background = itemView.context.getDrawable(
                        R.drawable.layerlist_trash)

                tvCountLeft.visibility = View.GONE
            }
        }

        fun removeFromList(plate : Plate){
            MainActivity.shopBox.removePlate(plate)
            notifyItemRemoved(adapterPosition);
            notifyItemRangeChanged(adapterPosition, 1);
        }

        fun toggleFav(plate : Plate){
            if(user.plateIsFav(plate)) {
                user.removeToFav(plate)
                vBtnIcon.background =
                    itemView.context.getDrawable(R.drawable.layerlist_favourite)
            }
            else {
                user.addToFav(plate)
                vBtnIcon.background = itemView.context.getDrawable(
                    R.drawable.layerlist_favourite_on)
            }
            sharedPrefManager.saveUser(itemView.context, user)
        }
    }

    interface OnClickPlate{
        fun onClickPlate(plate : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : BaseViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_plates_shop_card, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(plates[position])
    }

    override fun getItemCount(): Int = plates.size
}